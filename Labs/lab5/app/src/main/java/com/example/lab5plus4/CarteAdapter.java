package com.example.lab5plus4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CarteAdapter extends ArrayAdapter<CarteDeIdentitate> {

    private Context context;
    private LayoutInflater inflater;

    public CarteAdapter(Context context, ArrayList<CarteDeIdentitate> carti) {
        super(context, 0, carti);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarteDeIdentitate ci = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_carte, parent, false);
        }

        TextView tvNume = convertView.findViewById(R.id.tvNume);
        TextView tvDetalii = convertView.findViewById(R.id.tvDetalii);

        // Citim preferințele
        SharedPreferences prefs = context.getSharedPreferences("setari", Context.MODE_PRIVATE);
        int textSize = prefs.getInt("textSize", 16);
        String color = prefs.getString("textColor", "#000000");

        tvNume.setTextSize(textSize);
        tvDetalii.setTextSize(textSize);

        tvNume.setTextColor(Color.parseColor(color));
        tvDetalii.setTextColor(Color.parseColor(color));

        // Formatam data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataFormata = sdf.format(ci.getDataEmitere());

        tvNume.setText("Nume: " + ci.getNume());
        tvDetalii.setText(
                "Vârstă: " + ci.getVarsta() + "\n" +
                        "Căsătorit: " + (ci.isEsteCasatorit() ? "Da" : "Nu") + "\n" +
                        "Înălțime: " + ci.getInaltime() + " m\n" +
                        "Sex: " + ci.getSex() + "\n" +
                        "Data emiterii: " + dataFormata
        );

        return convertView;
    }
}
