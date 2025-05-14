package com.example.proiect;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextBio;
    DatePicker datePickerBirth;
    Spinner spinnerGender, spinnerInterestedIn;
    CheckBox checkboxTerms;
    Button buttonRegister, buttonSelectLocation;
    TextView textViewLoginLink, textViewSelectedLocation;
    DatabaseHelper dbHelper;
    private String detectedCity = "";
    double selectedLatitude = 0.0;
    double selectedLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextBio = findViewById(R.id.editTextBio);
        datePickerBirth = findViewById(R.id.datePickerBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerInterestedIn = findViewById(R.id.spinnerInterestedIn);
        checkboxTerms = findViewById(R.id.checkboxTerms);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonSelectLocation = findViewById(R.id.buttonSelectLocation);
        textViewLoginLink = findViewById(R.id.textViewLoginLink);
        textViewSelectedLocation = findViewById(R.id.textViewSelectedLocation);

        populateSpinners(); // pun toate valorile in spinere
        limitDatePicker();  // fac sa nu pot alege date din viitor

        buttonRegister.setEnabled(false);
        buttonRegister.setOnClickListener(v -> registerUser());

        // Cand apas butonul de alege locatia deschide maps activity
        buttonSelectLocation.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
            startActivityForResult(intent, 1);
        });

        // Cand bifez checkbox-ul se deblocheaza butonul de register
        checkboxTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonRegister.setEnabled(isChecked);
        });

        textViewLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });

        datePickerBirth.updateDate(2000, 0, 1);
    }

    // Populare spinere din formularul de register
    private void populateSpinners() {
        String[] genders = {"Barbat", "Femeie"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        spinnerGender.setAdapter(genderAdapter);

        String[] interests = {"Barbați", "Femei"};
        ArrayAdapter<String> interestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, interests);
        spinnerInterestedIn.setAdapter(interestAdapter);
    }

    // Asa nu aleg date mai tarzii de data actuala
    private void limitDatePicker() {
        Calendar calendar = Calendar.getInstance();
        datePickerBirth.setMaxDate(calendar.getTimeInMillis());
    }

    // Dupa completarea tuturor campurilor realizez procesul de inregistrare
    private void registerUser() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String bio = editTextBio.getText().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String interestedIn = spinnerInterestedIn.getSelectedItem().toString();
        String birthdate = datePickerBirth.getDayOfMonth() + "/" + (datePickerBirth.getMonth() + 1) + "/" + datePickerBirth.getYear();
        boolean termsAccepted = checkboxTerms.isChecked();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(bio)) {
            Toast.makeText(this, "Completeaza toate campurile!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!termsAccepted) {
            Toast.makeText(this, "Trebuie sa accepti termenii si conditiile!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, email, password, birthdate, detectedCity, gender, interestedIn, bio, "default_profile.jpg", selectedLatitude, selectedLongitude);

        boolean inserted = dbHelper.addUser(user);
        if (inserted) {
            Toast.makeText(this, "Inregistrare reusita!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Eraoare la inregistrare. Email-ul apare deja in baza de date!", Toast.LENGTH_SHORT).show();
        }
    }

    // Se apeleaza automat cand se termina maps activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Iau latitudinea si longitudinea din maps activity
            selectedLatitude = data.getDoubleExtra("latitude", 0.0);
            selectedLongitude = data.getDoubleExtra("longitude", 0.0);

            textViewSelectedLocation.setText("Locatie selectata: " + selectedLatitude + ", " + selectedLongitude);

            // Aici am folosit Geocoder sa pot sa imi iau orasul direct din latitudine si longitudine
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(selectedLatitude, selectedLongitude, 1);

                if (addresses != null && !addresses.isEmpty()) {
                    detectedCity = addresses.get(0).getLocality();
                    if (detectedCity == null) detectedCity = "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
