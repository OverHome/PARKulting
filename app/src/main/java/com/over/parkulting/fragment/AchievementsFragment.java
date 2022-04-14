package com.over.parkulting.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.over.parkulting.R;
import com.over.parkulting.adapter.PointAdapter;
import com.over.parkulting.object.GeoPoint;
import com.over.parkulting.tools.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {

    SQLiteDatabase db;
    RecyclerView rv_achiev;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_achievement, container, false);
        rv_achiev = root.findViewById(R.id.rv_achiev);
        ConstraintLayout no_ach_info = root.findViewById(R.id.no_ach_info);
        List<GeoPoint> pointsl = new ArrayList<>();
        db = DBHelper.connectDB(getContext());

        Cursor cursor = db.rawQuery("SELECT * FROM points_in_park", null);
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
            if (posit){
                pointsl.add(new GeoPoint(name, h, d, posit));
            }
            cursor.moveToNext();
        }

        if (pointsl.size()==0) no_ach_info.setVisibility(View.VISIBLE);
        else {
            PointAdapter adapter = new PointAdapter(getContext(), pointsl);
            rv_achiev.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_achiev.setAdapter(adapter);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}