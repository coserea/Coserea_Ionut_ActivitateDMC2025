package com.example.proiect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ListView listViewUsers;
    private DatabaseHelper databaseHelper;
    private List<User> userList;
    private UserAdapter userAdapter;
    private Button buttonLogout, buttonStatistics;
    private int currentUserId;
    private double currentLatitude, currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUserId = getIntent().getIntExtra("userId", -1); // id luat din login (user actual)

        listViewUsers = findViewById(R.id.listViewUsers);
        databaseHelper = new DatabaseHelper(this);

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logoutUser()); // apelez functia de logout la delogare

        buttonStatistics = findViewById(R.id.buttonStatistics); // deschid activitatea de statistici
        buttonStatistics.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });

        userList = databaseHelper.getAllUsers(); // incarc lista de utilizatori

        // doua chestii se intampla aici
        // 1. iau latitudinea si longitudinea user-ului actual pentru a calcula distanta pana la ceilalti useri
        // 2. sterg din lista user-ul actual pentru a nu afisa (in lista din home nu apar si eu ca utilizator actual)
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() == currentUserId) {
                currentLatitude = userList.get(i).getLatitude();
                currentLongitude = userList.get(i).getLongitude();
                userList.remove(i);
                break;
            }
        }

        // Pentru fiecare user calculez numarul de like-uri
        for (User user : userList) {
            int likesCount = databaseHelper.getLikesCount(user.getId());
            user.setLikesCount(likesCount);
        }

        // Setez user adapter-ul pe lista de useri actualizata
        userAdapter = new UserAdapter(this, userList, currentUserId, currentLatitude, currentLongitude);
        listViewUsers.setAdapter(userAdapter);
    }

    // Cand dou log-out sterg din user preferences credentialele actuale si ma intorc in login
    private void logoutUser() {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
