package com.over.parkulting.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.over.parkulting.tools.DBHelper;
import com.over.parkulting.activity.PointActivity;
import com.over.parkulting.adapter.ParkAdapter;
import com.over.parkulting.databinding.FragmentHomeBinding;
import com.over.parkulting.object.Park;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Park> parkArrayList;
    private ParkAdapter adapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ListView routeList = binding.routeList;
        setRoute();



        routeList.setAdapter(adapter);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PointActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });

        return root;
    }

    public void setRoute(){
        parkArrayList = new ArrayList<Park>();
        db = DBHelper.connectDB(getContext());
        Cursor cursor = db.rawQuery("SELECT * FROM parks", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            parkArrayList.add(new Park(cursor.getString(1)));//cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        adapter = new ParkAdapter(getContext(), parkArrayList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}