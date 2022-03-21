package com.over.parkulting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.over.parkulting.DBHelper;
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

        ListView pointlist = findViewById(R.id.point_list);
        List<GeoPoint> pointsl = new ArrayList<>();
        pointsl.add(new GeoPoint("ytxnj", 12.99, 12.99));
        pointsl.add(new GeoPoint("ytxnj", 12.99, 12.99));
        PointAdapter adapter = new PointAdapter(this, pointsl);
        pointlist.setAdapter(adapter);
    }
}