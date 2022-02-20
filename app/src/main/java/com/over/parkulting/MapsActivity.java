package com.over.parkulting;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.over.parkulting.R;
import com.over.parkulting.databinding.ActivityMapsBinding;
import com.over.parkulting.DBHelper;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private int pointId;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent i = getIntent();
        if (i != null) {
            pointId= i.getIntExtra("point", 0);
        }
        connectDB();
        String location;
        Cursor pointCursor = db.rawQuery("SELECT * FROM location_points where point_id = "+(pointId), null);
        pointCursor.moveToFirst();
        location = pointCursor.getString(1);

        mMap = googleMap;
        //  mMap.setMyLocationEnabled(true);
        List<Address> list = null;
        try {
            list = new Geocoder(this).getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double x = list.get(0).getLongitude();
        double y = list.get(0).getLatitude();

        LatLng sydney = new LatLng(y, x);
        mMap.addMarker(new MarkerOptions().position(sydney).title(location));
        mMap.setMinZoomPreference(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    public void connectDB(){
        dbHelper = new DBHelper(this);
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }
}