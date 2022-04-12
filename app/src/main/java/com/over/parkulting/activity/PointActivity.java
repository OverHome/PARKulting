package com.over.parkulting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.over.parkulting.object.Park;
import com.over.parkulting.tools.DBHelper;
import com.over.parkulting.R;
import com.over.parkulting.adapter.PointAdapter;
import com.over.parkulting.object.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class PointActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private int roudeId;
    private String[] points;
    private Integer[] isTake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        db = DBHelper.connectDB(this);
        Intent i = getIntent();
        if (i != null) {
            roudeId = i.getIntExtra("position", 0)+1;
        }

        RecyclerView pointlist = findViewById(R.id.point_list);
        List<GeoPoint> pointsl = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM points_in_park WHERE park_id = "+ roudeId, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(2);
            Cursor cursor1 = db.rawQuery("SELECT * FROM location_points WHERE point_id = "+ cursor.getInt(0), null);
            cursor1.moveToFirst();
            double h = cursor1.getDouble(2);
            double d = cursor1.getDouble(1);
            cursor1 = db.rawQuery("SELECT * FROM user_points WHERE point_id = "+ cursor.getInt(0), null);
            cursor1.moveToFirst();
            boolean posit = 1 == cursor1.getInt(1);
            pointsl.add(new GeoPoint(name, h, d, posit));
            cursor.moveToNext();
        }

        PointAdapter adapter = new PointAdapter(this, pointsl);
        pointlist.setLayoutManager(new LinearLayoutManager(this));
        pointlist.setAdapter(adapter);
    }
}