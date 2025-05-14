package com.example.proiect;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private FrameLayout chartGenderContainer, chartCityContainer;
    private LinearLayout legendGender, legendCity;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        chartGenderContainer = findViewById(R.id.chartGenderContainer);
        chartCityContainer = findViewById(R.id.chartCityContainer);
        legendGender = findViewById(R.id.legendGender);
        legendCity = findViewById(R.id.legendCity);

        databaseHelper = new DatabaseHelper(this);

        showGenderChart();
        showCityChart();
    }

    // PieChart cu proportiile Barbati/Femei care folosesc aplicatia
    private void showGenderChart() {
        List<User> users = databaseHelper.getAllUsers();
        int males = 0, females = 0;

        for (User user : users) {
            if ("Barbat".equalsIgnoreCase(user.getGender())) {
                males++;
            } else if ("Femeie".equalsIgnoreCase(user.getGender())) {
                females++;
            }
        }

        ArrayList<Float> values = new ArrayList<>();
        values.add((float) males);
        values.add((float) females);

        CustomChartView chartView = new CustomChartView(this, values, "PieChart");
        chartGenderContainer.addView(chartView);

        addLegendItem(legendGender, "Barbati: " + males, "#FF0000"); // roșu
        addLegendItem(legendGender, "Femei: " + females, "#0000FF"); // albastru
    }

    private void showCityChart() {
        List<User> users = databaseHelper.getAllUsers();
        // Fac un Map nume valoare ca de exemplu sa am Bucuresti 2 --> 2 useri din Bucuresti
        // E mai usor de lucrat asa pentru afisarea PieChart
        Map<String, Integer> cityCountMap = new HashMap<>();

        for (User user : users) {
            String city = user.getCity() != null ? user.getCity() : "Necunoscut";
            cityCountMap.put(city, cityCountMap.getOrDefault(city, 0) + 1);
        }

        ArrayList<Float> values = new ArrayList<>();
        for (Integer val : cityCountMap.values()) {
            values.add(val.floatValue());
        }

        CustomChartView chartView = new CustomChartView(this, values, "PieChart");
        chartCityContainer.addView(chartView);

        String[] colors = {"#FF0000", "#0000FF", "#00FF00", "#FFFF00", "#FF00FF", "#00FFFF", "#FFA500"};
        int colorIndex = 0;

        for (Map.Entry<String, Integer> entry : cityCountMap.entrySet()) {
            addLegendItem(legendCity, entry.getKey() + ": " + entry.getValue() + " utilizatori", colors[colorIndex % colors.length]);
            colorIndex++;
        }
    }

    // Creez un text view si il adaug intr-un linear laytout, fiecare felie din pychar are o culoare (aceleasi cu legenda)
    private void addLegendItem(LinearLayout container, String text, String colorHex) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTextColor(android.graphics.Color.parseColor(colorHex));
        textView.setPadding(8, 8, 8, 8);
        container.addView(textView);
    }
}
