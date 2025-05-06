package com.example.lab8;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ListView lv;
    private ArrayAdapter<Student> adapter;
    private List<Student> favs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_favorites);

        lv = findViewById(R.id.lvFavorites);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                favs);
        lv.setAdapter(adapter);

        FirebaseDatabase.getInstance(
                        "https://lab8android123-default-rtdb.firebaseio.com/")
                .getReference("students")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        favs.clear();
                        for (DataSnapshot c : snap.getChildren()) {
                            Student s = c.getValue(Student.class);
                            favs.add(s);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override public void onCancelled(DatabaseError e) { }
                });
    }
}
