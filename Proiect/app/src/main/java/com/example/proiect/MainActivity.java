package com.example.proiect;

import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText editTextLoginEmail, editTextLoginPassword;
    Button buttonLogin;
    TextView textViewRegisterLink, textViewQuote;
    DatabaseHelper databaseHelper;
    CheckBox checkboxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // dezactivez dark mode --> ma deranja vizual cand eram la mine pe telefon
        // (imi schimba culorile si nu mi-a iesit sa fac temele individuale in /res/values/themes
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);

        // Verific aici daca fisierul e deja logat
        // Daca fisierul de user preferences nu e gol si are credentiale intru direct in home activity
        if (isUserLoggedIn()) {
            int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("userId", -1);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
            return;
        }

        // Daca nu sunt logat deja incep sa construiesc pagina de login
        setContentView(R.layout.activity_main);
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegisterLink = findViewById(R.id.textViewRegisterLink);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        textViewQuote = findViewById(R.id.textViewQuote);

        new FetchQuoteTask(textViewQuote).execute(); // Pentru cerinta cu json pornesc task-ul de citate random

        databaseHelper = new DatabaseHelper(this); // Initializare baza de date

        // La apasarea butonului de login se initializeaza logica de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Logica apasarii link-ului de register
        textViewRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigare in Register
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Logica de login
    // Verificam sa fie casetele completate
    // Verificam daca credentialele corespund cu cineva din baza de date                    |
    //                                                                                      | --> login si redirectionare in home activity
    // Daca am bifata casuta de tine-ma minte se salveaza credentialele in user preferences |
    // Daca datele nu corespund primesc un Toast de avertizare
    private void loginUser() {
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Completeaza email si parola!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean checkUser = databaseHelper.checkUser(email, password);

        if (checkUser) {
            Toast.makeText(this, "Login reusit!", Toast.LENGTH_SHORT).show();
            int userId = databaseHelper.getUserId(email);

            if (checkboxRememberMe.isChecked()) {
                saveUserLogin(userId);
            }

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Email sau parola incorecte!", Toast.LENGTH_SHORT).show();
        }
    }

    // Salvare in user preferences
    private void saveUserLogin(int userId) {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putInt("userId", userId)
                .apply();
    }

    // Aici verific fisierul de user preferences sa vad daca sunt deja logat
    private boolean isUserLoggedIn() {
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("userId", -1);
        return userId != -1;
    }

    // Clasa privata pentru citate folosita doar in main activity
    private static class FetchQuoteTask extends AsyncTask<Void, Void, String> {

        private final TextView textView;

        public FetchQuoteTask(TextView textView) {
            this.textView = textView;
        }

        // Aceasta functie arata logica din spate a citatelor
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Deschid conexiunea la link-ul unde se afla JSON-ul cu citatele
                URL url = new URL("https://gist.githubusercontent.com/coserea/55e619cf05bb360239c680e4160a3817/raw/quotes.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Se citeste text-ul ce vine de la acel link linie cu linie
                // Se initializeaza result si se pune in ea fiecare linie citita
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();

                JSONObject jsonObject = new JSONObject(result.toString()); // Fac din String --> obiect JSON
                JSONArray quotesArray = jsonObject.getJSONArray("quotes"); // Iau din JSON array-ul de citate

                // Aleg un citat random si dupa il returnez pentru afusare
                int randomIndex = (int) (Math.random() * quotesArray.length());
                return quotesArray.getString(randomIndex);

            } catch (Exception e) {
                e.printStackTrace();
                return "Citatul nu este disponibil in acest moment.";
            }
        }

        // Dupa ce s-a terminat functia de mai sus citatul e afisat in thread-ul principal
        @Override
        protected void onPostExecute(String quote) {
            textView.setText(quote);
        }
    }
}