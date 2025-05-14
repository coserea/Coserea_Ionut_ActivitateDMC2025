package com.example.proiect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean firstFocusDone = false; // daca nu puneam asta imi dadea focus de mai multe ori

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Verific daca aplicatia are permisiuni de locatie, daca nu le cere
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true); // locatia mea actuala (sageata albastra)

        // Focus pe locatia mea actuala
        // Zoom-ul se pune cand se afla locatia
        // Nu pun Marker automat
        mMap.setOnMyLocationChangeListener(location -> {
            if (!firstFocusDone) {
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                focusCameraOnLocation(userLatLng);
                firstFocusDone = true;
            }
        });

        // Vreau sa pun marker doar cand apas pe harta
        // Dupa ce apas se inchide maps activity si se trimit latitudinea si longitudinea la register
        mMap.setOnMapClickListener(latLng -> {
            moveMarker(latLng);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitude", latLng.latitude);
            resultIntent.putExtra("longitude", latLng.longitude);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void moveMarker(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Locatie selectata"));
        focusCameraOnLocation(latLng);
    }

    // Functia de focus camera pe locatie
    private void focusCameraOnLocation(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
    }
}
