package com.example.cosereaionutc114a;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;

public class NewHusa extends AppCompatActivity {

    private EditText editMaterial, editLungime;
    private CheckBox checkCustomizable;
    private RadioGroup radioColor;
    private Button byttonTrimite;

    private Date dataEmitere;
    private int pozitieEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_husa);

        // Inițializari UI
        editMaterial = findViewById(R.id.coserea_ionut_editTextMaterial);
        editLungime = findViewById(R.id.coserea_ionut_editTextLungime);
        checkCustomizable = findViewById(R.id.coserea_ionut_checkBoxCustomizable);
        radioColor = findViewById(R.id.coserea_ionut_radioCuloare);
        byttonTrimite = findViewById(R.id.coserea_ionut_buttonTrimite);

        // Trimite obiectul
        byttonTrimite.setOnClickListener(v -> {
            String material = editMaterial.getText().toString();
            int lungime = Integer.parseInt(editLungime.getText().toString());
            boolean customizable = checkCustomizable.isChecked();

            CeCuloare culoare = CeCuloare.ROSU;
            if (radioColor.getCheckedRadioButtonId() == R.id.radioNegru) {
                culoare = CeCuloare.NEGRU;
            }

            if (radioColor.getCheckedRadioButtonId() == R.id.radioVerde) {
                culoare = CeCuloare.VERDE;
            }

            if (radioColor.getCheckedRadioButtonId() == R.id.radioRosu) {
                culoare = CeCuloare.ROSU;
            }

            // Cream obiectul
            HusaTelefon ht = new HusaTelefon(material, lungime, customizable, culoare);

            // il trimitem inapoi
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ht", ht);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}