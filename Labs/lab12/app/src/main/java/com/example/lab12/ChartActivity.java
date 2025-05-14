package com.example.lab12;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private FrameLayout chartContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chartContainer = findViewById(R.id.chartContainer);

        ArrayList<Float> valori = (ArrayList<Float>) getIntent().getSerializableExtra("valori");
        String chartType = getIntent().getStringExtra("chartType");

        CustomChartView customChartView = new CustomChartView(this, valori, chartType);
        chartContainer.addView(customChartView);
    }
}
