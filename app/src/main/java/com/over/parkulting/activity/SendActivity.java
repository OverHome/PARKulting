package com.over.parkulting.activity;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.over.parkulting.R;
import com.over.parkulting.tools.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Parkulting);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        Button gyu = findViewById(R.id.buttontyut);

        Spinner spinner = findViewById(R.id.spinner);
        List<String> park = new ArrayList<>();
        SQLiteDatabase db = DBHelper.connectDB(this);
        Cursor cursor = db.rawQuery("SELECT * FROM parks", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            park.add(cursor.getString(1));
            cursor.moveToNext();
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, park);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SQLiteDatabase db = DBHelper.connectDB(getApplicationContext());
                List<String> point = new ArrayList<>();
                Cursor cursor = db.rawQuery("SELECT * FROM points_in_park", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    point.add(cursor.getString(2));
                    cursor.moveToNext();
                }
                Spinner spinner2 = findViewById(R.id.spinner2);
                ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                        (getApplicationContext(), android.R.layout.simple_spinner_item, point);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner2.setAdapter(spinnerArrayAdapter2);
            }
        });


        List<String> point = new ArrayList<>();
        cursor = db.rawQuery("SELECT * FROM points_in_park", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            point.add(cursor.getString(2));
            cursor.moveToNext();
        }
        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, point);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerArrayAdapter2);

        gyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}