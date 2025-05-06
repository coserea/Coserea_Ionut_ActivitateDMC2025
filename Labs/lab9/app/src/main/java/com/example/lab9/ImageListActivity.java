package com.example.lab9;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class ImageListActivity extends AppCompatActivity {

    ListView listView;

    String[] imageUrls = {
            "https://frankfurt.apollo.olxcdn.com/v1/files/g85uuaohpezv-RO/image;s=1000x700",
            "https://frankfurt.apollo.olxcdn.com/v1/files/jpo9l0sdmsrk2-RO/image;s=1000x700",
            "https://frankfurt.apollo.olxcdn.com/v1/files/ox9hgmw0nmoi1-RO/image;s=1000x700",
            "https://frankfurt.apollo.olxcdn.com/v1/files/6a5tbx72sk063-RO/image;s=1000x700",
            "https://frankfurt.apollo.olxcdn.com/v1/files/buq4iydwycq52-RO/image;s=1000x700"
    };

    String[] descriptions = {
            "Lamborghini Premium 1060",
            "Autopropulsata Berthoud Boxer",
            "Tractor U650 cu plug",
            "Tractor Case Maxxum 5140",
            "Tractor U-650 disponibil"
    };

    String[] links = {
            "https://www.olx.ro/d/oferta/lamborghini-premium-1060-IDjirwh.html",
            "https://www.olx.ro/d/oferta/vand-autopropulsata-berthoud-boxer-IDjmmrx.html",
            "https://www.olx.ro/d/oferta/vind-tractor-u650-cu-plug-IDjlrFC.html",
            "https://www.olx.ro/d/oferta/tractor-case-maxxum-5140-cu-compresor-aer-IDjn7ny.html",
            "https://www.olx.ro/d/oferta/tractor-u-650-disponibil-IDjo0ZV.html"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, imageUrls, descriptions, links);
        listView.setAdapter(adapter);
    }
}
