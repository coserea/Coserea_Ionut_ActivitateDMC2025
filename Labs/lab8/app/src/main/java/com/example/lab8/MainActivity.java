package com.example.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editName, editAge, editSearchName,
            editMinAge, editMaxAge,
            editDeleteAge, editInitiala;
    Button btnAdd, btnShowAll, btnIncreaseAge,
            btnSearchByName, btnFilterByAge,
            btnDeleteLessThan, btnOpenAdd,
            btnOpenFavorites;
    LinearLayout listContainer;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // bind
        editName        = findViewById(R.id.editName);
        editAge         = findViewById(R.id.editAge);
        editSearchName  = findViewById(R.id.editSearchName);
        editMinAge      = findViewById(R.id.editMinAge);
        editMaxAge      = findViewById(R.id.editMaxAge);
        editDeleteAge   = findViewById(R.id.editDeleteAge);
        editInitiala    = findViewById(R.id.editInitiala);

        btnAdd          = findViewById(R.id.btnAdd);
        btnShowAll      = findViewById(R.id.btnShowAll);
        btnSearchByName = findViewById(R.id.btnSearchByName);
        btnFilterByAge  = findViewById(R.id.btnFilterByAge);
        btnDeleteLessThan = findViewById(R.id.btnDeleteLessThan);
        btnIncreaseAge  = findViewById(R.id.btnIncreaseAge);

        btnOpenAdd      = findViewById(R.id.btnOpenAdd);
        btnOpenFavorites= findViewById(R.id.btnOpenFavorites);

        listContainer   = findViewById(R.id.listContainer);

        // Operații SQLite
        btnAdd.setOnClickListener(v -> {
            String n = editName.getText().toString().trim();
            String a = editAge.getText().toString().trim();
            if (n.isEmpty()||a.isEmpty()) {
                Toast.makeText(this,
                        "Completează toate câmpurile",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int age = Integer.parseInt(a);
            db.insertStudent(n, age);
            Toast.makeText(this,
                    "Salvat local!", Toast.LENGTH_SHORT).show();
            editName.setText("");
            editAge.setText("");
        });
        btnShowAll.setOnClickListener(v ->
                afiseazaLista(db.getAllStudents()));
        btnSearchByName.setOnClickListener(v -> {
            String n = editSearchName.getText().toString().trim();
            Student s = db.getStudentByName(n);
            if (s!=null) afiseazaLista(List.of(s));
            else Toast.makeText(this,
                    "Student nu găsit", Toast.LENGTH_SHORT).show();
        });
        btnFilterByAge.setOnClickListener(v -> {
            String mn = editMinAge.getText().toString().trim();
            String mx = editMaxAge.getText().toString().trim();
            if (mn.isEmpty()||mx.isEmpty()) return;
            afiseazaLista(
                    db.getStudentsBetweenAges(
                            Integer.parseInt(mn),
                            Integer.parseInt(mx)
                    )
            );
        });
        btnDeleteLessThan.setOnClickListener(v -> {
            String t = editDeleteAge.getText().toString().trim();
            if (t.isEmpty()) return;
            db.deleteStudentsByAgeCondition("<",
                    Integer.parseInt(t));
            afiseazaLista(db.getAllStudents());
        });
        btnIncreaseAge.setOnClickListener(v -> {
            String l = editInitiala.getText().toString().trim();
            if (!l.isEmpty()) {
                db.increaseAgeForNameStartingWith(l);
                Toast.makeText(this,
                        "Varsta crescuta!", Toast.LENGTH_SHORT).show();
                afiseazaLista(db.getAllStudents());
            }
        });

        // Navigare catre online
        btnOpenAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddActivity.class)));
        btnOpenFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, FavoritesActivity.class)));

        // Firebase listener
        FirebaseDatabase.getInstance("https://lab8android123-default-rtdb.firebaseio.com/")
                .getReference("students")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        Toast.makeText(MainActivity.this,
                                "Firebase actualizat (" +
                                        snap.getChildrenCount() + ")",
                                Toast.LENGTH_SHORT).show();
                        afiseazaLista(db.getAllStudents());
                    }
                    @Override public void onCancelled(DatabaseError e) {
                        Log.w("MainActivity", e.getMessage());
                    }
                });
    }

    private void afiseazaLista(List<Student> list) {
        listContainer.removeAllViews();
        for (Student s : list) {
            TextView tv = new TextView(this);
            tv.setText(s.toString());
            tv.setTextSize(16);
            tv.setPadding(8,8,8,8);
            listContainer.addView(tv);
        }
    }
}
