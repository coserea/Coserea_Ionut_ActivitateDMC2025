package com.example.lab5plus4;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class SettingsActivity2 extends AppCompatActivity {

    private EditText editTextSize;
    private Spinner spinnerColor;
    private Button btnSave;
    String[] numeCulori = {"Negru", "Roșu", "Albastru", "Verde"};
    String[] coduriCulori = {"#000000", "#FF0000", "#0000FF", "#008000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);

        editTextSize = findViewById(R.id.editTextSize);
        spinnerColor = findViewById(R.id.spinnerColor);
        btnSave = findViewById(R.id.btnSaveSettings);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numeCulori);
        spinnerColor.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            String sizeStr = editTextSize.getText().toString().trim();

            if (!sizeStr.matches("\\d+")) {
                Toast.makeText(this, "Introduceți o dimensiune valida!", Toast.LENGTH_SHORT).show();
                return;
            }

            int size = Integer.parseInt(sizeStr);
            int pozitieSelectata = spinnerColor.getSelectedItemPosition();
            String codHex = coduriCulori[pozitieSelectata];

            SharedPreferences prefs = getSharedPreferences("setari", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("textSize", size);
            editor.putString("textColor", codHex);
            editor.apply();

            setResult(RESULT_OK);
            finish();
        });

    }
}
