package com.example.lab5plus4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_CI = 200;
    private ArrayList<CarteDeIdentitate> ciList;
    private CarteAdapter adapter;
    private ListView listView;
    private Button btnOpenForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ciList = new ArrayList<>();

        listView = findViewById(R.id.listViewCI);
        btnOpenForm = findViewById(R.id.btnOpenForm);

        adapter = new CarteAdapter(this, ciList);
        listView.setAdapter(adapter);

        // buton setari
        Button btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity2.class);
            startActivity(intent);
        });

        // Adaugare obiect nou
        btnOpenForm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCiActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CI);
        });

        // Editare obiect existent (click simplu)
        listView.setOnItemClickListener((parent, view, position, id) -> {
            CarteDeIdentitate ci = ciList.get(position);
            Intent intent = new Intent(MainActivity.this, AddCiActivity.class);
            intent.putExtra("ci",(Parcelable) ci);
            intent.putExtra("position", position);
            startActivityForResult(intent, REQUEST_CODE_ADD_CI);
        });

        // Ștergere obiect (click lung)
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            CarteDeIdentitate carte = ciList.get(position);
            salveazaInFavorite(carte);
            Toast.makeText(this, "Adăugat la favorite!", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    // Preluare rezultat din AddCiActivity (adaugare sau modificare)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_CI && resultCode == RESULT_OK && data != null) {
            CarteDeIdentitate ci = data.getParcelableExtra("ci");
            int position = data.getIntExtra("position", -1);

            if (position == -1) {
                // Obiect nou
                ciList.add(ci);
            } else {
                // Modificare obiect existent
                ciList.set(position, ci);
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void salveazaInFavorite(CarteDeIdentitate carte) {
        try {
            FileOutputStream fos = openFileOutput("favorite.txt", MODE_APPEND);
            AppendableObjectOutputStream oos = new AppendableObjectOutputStream(fos);
            oos.writeObject(carte);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
