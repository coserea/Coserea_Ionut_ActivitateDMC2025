package com.example.lab8;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class AddActivity extends AppCompatActivity {

    private EditText etName, etAge;
    private CheckBox cbOnline;
    private Button btnSave;
    private DatabaseHelper db;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_add);

        etName  = findViewById(R.id.etName);
        etAge   = findViewById(R.id.etAge);
        cbOnline= findViewById(R.id.cbOnline);
        btnSave = findViewById(R.id.btnSave);

        db = new DatabaseHelper(this);
        ref = FirebaseDatabase.getInstance(
                        "https://lab8android123-default-rtdb.firebaseio.com/")
                .getReference("students");

        btnSave.setOnClickListener(v -> {
            String n = etName.getText().toString().trim();
            String a = etAge.getText().toString().trim();
            if (n.isEmpty()||a.isEmpty()) {
                Toast.makeText(this,
                        "Completeaza toate campurile",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int age = Integer.parseInt(a);
            db.insertStudent(n, age);
            Toast.makeText(this,
                    "Salvat local", Toast.LENGTH_SHORT).show();

            if (cbOnline.isChecked()) {
                Student sObj = new Student();
                sObj.setNume(n);
                sObj.setVarsta(age);
                ref.push()
                        .setValue(sObj)
                        .addOnSuccessListener(x ->
                                Toast.makeText(this,
                                        "Salvat în Firebase",
                                        Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(this,
                                        "Eroare: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show()
                        );
            }

            etName.setText("");
            etAge.setText("");
            cbOnline.setChecked(false);
        });
    }
}
