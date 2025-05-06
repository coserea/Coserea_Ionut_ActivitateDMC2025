package com.example.lab5plus4;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import android.content.SharedPreferences;
import android.graphics.Color;
import java.io.FileOutputStream;

public class AddCiActivity extends AppCompatActivity {

    private EditText editNume, editVarsta, editInaltime;
    private CheckBox checkCasatorit;
    private RadioGroup radioSex;
    private Button btnDataEmitere, btnTrimite;

    private Date dataEmitere;
    private int pozitieEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ci);

        // Inițializari UI
        editNume = findViewById(R.id.editTextNume);
        editVarsta = findViewById(R.id.editTextVarsta);
        editInaltime = findViewById(R.id.editTextInaltime);
        checkCasatorit = findViewById(R.id.checkBoxCasatorit);
        radioSex = findViewById(R.id.radioGroupSex);
        btnDataEmitere = findViewById(R.id.buttonDataEmitere);
        btnTrimite = findViewById(R.id.buttonTrimite);

        // Verificam dacă venim pentru editare
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ci")) {
            CarteDeIdentitate ci = intent.getParcelableExtra("ci");
            pozitieEdit = intent.getIntExtra("position", -1);

            if (ci != null) {
                // Precompletare câmpuri
                editNume.setText(ci.getNume());
                editVarsta.setText(String.valueOf(ci.getVarsta()));
                editInaltime.setText(String.valueOf(ci.getInaltime()));
                checkCasatorit.setChecked(ci.isEsteCasatorit());
                dataEmitere = ci.getDataEmitere();

                if (ci.getSex() == TipSex.FEMININ) {
                    radioSex.check(R.id.radioFeminin);
                } else {
                    radioSex.check(R.id.radioMasculin);
                }
            }
        }

        // Selectare data emitere
        btnDataEmitere.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance();
            if (dataEmitere != null) {
                cal.setTime(dataEmitere);
            }

            int an = cal.get(Calendar.YEAR);
            int luna = cal.get(Calendar.MONTH);
            int zi = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddCiActivity.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dataEmitere = cal.getTime();
                    },
                    an, luna, zi
            );

            datePickerDialog.show();
        });

        // Trimite obiectul
        btnTrimite.setOnClickListener(v -> {
            String nume = editNume.getText().toString();
            int varsta = Integer.parseInt(editVarsta.getText().toString());
            double inaltime = Double.parseDouble(editInaltime.getText().toString());
            boolean casatorit = checkCasatorit.isChecked();

            if (dataEmitere == null) {
                dataEmitere = new Date(); // setăm data curentă dacă nu a fost aleasă
            }

            TipSex sex = TipSex.MASCULIN;
            if (radioSex.getCheckedRadioButtonId() == R.id.radioFeminin) {
                sex = TipSex.FEMININ;
            }

            // Cream obiectul
            CarteDeIdentitate ci = new CarteDeIdentitate(nume, varsta, casatorit, inaltime, sex, dataEmitere);

            // il trimitem inapoi
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ci", (Parcelable) ci);
            resultIntent.putExtra("position", pozitieEdit);
            salveazaInFisier(ci);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void salveazaInFisier(CarteDeIdentitate carte) {
        try {
            FileOutputStream fos = openFileOutput("carti.txt", MODE_APPEND);
            AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos);
            oos.writeObject(carte);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
