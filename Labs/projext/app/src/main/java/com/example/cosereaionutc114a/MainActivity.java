package com.example.cosereaionutc114a;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_HT = 200;
    private ArrayList<HusaTelefon> htList;
    private Button btnOpenForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        htList = new ArrayList<>();

        ListView listView = findViewById(R.id.coserea_ionut_listViewHusa);
        btnOpenForm = findViewById(R.id.coserea_ionut_btnOpenForm);

        // Adaugare obiect nou
        btnOpenForm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewHusa.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_HT);
        });
    }

    // Preluare rezultat din AddCiActivity (adaugare sau modificare)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_HT && resultCode == RESULT_OK && data != null) {
            HusaTelefon ht = data.getParcelableExtra("ht");
            int position = data.getIntExtra("position", -1);

            if (position == -1) {
                // Obiect nou
                htList.add(ht);
            } else {
                // Modificare obiect existent
                htList.set(position, ht);
            }
        }
    }
}