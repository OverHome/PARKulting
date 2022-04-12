package com.over.parkulting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

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
            roudeId = i.getIntExtra("position", 0);
        }

        RecyclerView pointlist = findViewById(R.id.point_list);
        List<GeoPoint> pointsl = new ArrayList<>();

        pointsl.add(new GeoPoint("ytxnj", 12.99, 12.99, true));
        pointsl.add(new GeoPoint("ytxnj", 12.99, 12.99, false));

        PointAdapter adapter = new PointAdapter(this, pointsl);
        pointlist.setLayoutManager(new LinearLayoutManager(this));
        pointlist.setAdapter(adapter);
    }
}