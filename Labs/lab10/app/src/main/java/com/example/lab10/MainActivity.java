package com.example.lab10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText etCityName;
    private Spinner spinnerDays;
    private Button btnGetWeather;
    private TextView tvResult;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Edge-to-Edge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiKey = getString(R.string.accuweather_api_key);

        // View-uri
        etCityName    = findViewById(R.id.etCityName);
        spinnerDays   = findViewById(R.id.spinnerDays);
        btnGetWeather = findViewById(R.id.btnGetWeather);
        tvResult      = findViewById(R.id.tvResult);

        btnGetWeather.setOnClickListener(view -> {
            String city = etCityName.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(MainActivity.this, "Introduceti un oras", Toast.LENGTH_SHORT).show();
                return;
            }
            int days = Integer.parseInt(
                    spinnerDays.getSelectedItem().toString().split(" ")[0]
            );
            new CitySearchTask(days).execute(city);
        });
    }

    private class CitySearchTask extends AsyncTask<String, Void, String> {
        private final int daysForecast;
        CitySearchTask(int days) { this.daysForecast = days; }

        @Override
        protected String doInBackground(String... params) {
            try {
                String city = params[0];
                String urlStr = "http://dataservice.accuweather.com/locations/v1/cities/search"
                        + "?apikey=" + apiKey
                        + "&q=" + city;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONArray arr = new JSONArray(sb.toString());
                if (arr.length() > 0) {
                    return arr.getJSONObject(0).getString("Key");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cityKey) {
            if (cityKey == null) {
                tvResult.setText("Oras inexistent");
            } else {
                tvResult.setText("Cod oras: " + cityKey);
                new ForecastTask(daysForecast).execute(cityKey);
            }
        }
    }

    private class ForecastTask extends AsyncTask<String, Void, JSONObject> {
        private final int days;
        ForecastTask(int days) { this.days = days; }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                String cityKey = params[0];
                String endpoint;
                switch (days) {
                    case 5:  endpoint = "forecasts/v1/daily/5day/"; break;
                    case 10: endpoint = "forecasts/v1/daily/10day/"; break;
                    default: endpoint = "forecasts/v1/daily/1day/";
                }
                String urlStr = "http://dataservice.accuweather.com/" + endpoint
                        + cityKey
                        + "?apikey=" + apiKey
                        + "&metric=true";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return new JSONObject(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json == null) {
                tvResult.append("\nEroare la obținerea forecast-ului");
                return;
            }
            try {
                StringBuilder out = new StringBuilder();
                JSONArray daysArr = json.getJSONArray("DailyForecasts");
                for (int i = 0; i < daysArr.length(); i++) {
                    JSONObject d = daysArr.getJSONObject(i);
                    JSONObject t = d.getJSONObject("Temperature");
                    double min = t.getJSONObject("Minimum").getDouble("Value");
                    double max = t.getJSONObject("Maximum").getDouble("Value");
                    String date = d.getString("Date").substring(0, 10);
                    out.append(String.format("%s → Min: %.1f°C, Max: %.1f°C\n", date, min, max));
                }
                tvResult.append("\n\nForecast pentru " + days + " zile:\n" + out);
            } catch (JSONException e) {
                e.printStackTrace();
                tvResult.append("\nEroare parsare JSON");
            }
        }
    }
}