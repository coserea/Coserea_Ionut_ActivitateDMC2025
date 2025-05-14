package com.example.lab12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerChartType;
    private Button btnOpenChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerChartType = findViewById(R.id.spinnerChartType);
        btnOpenChart = findViewById(R.id.btnOpenChart);

        btnOpenChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChartActivity();
            }
        });
    }

    private void openChartActivity() {
        ArrayList<Float> valori = new ArrayList<>();
        Random random = new Random();
        int n = random.nextInt(5) + 3;

        for (int i = 0; i < n; i++) {
            valori.add(random.nextFloat() * 100);
        }

        String chartType = spinnerChartType.getSelectedItem().toString();

        Intent intent = new Intent(MainActivity.this, ChartActivity.class);
        intent.putExtra("valori", valori);
        intent.putExtra("chartType", chartType);
        startActivity(intent);
    }
}
