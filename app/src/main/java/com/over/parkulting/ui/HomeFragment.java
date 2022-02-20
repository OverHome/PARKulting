package com.over.parkulting.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.over.parkulting.DBHelper;
import com.over.parkulting.databinding.FragmentHomeBinding;
import com.over.parkulting.ui.RoutPoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String[] routeArray;
    private ArrayAdapter<String> adapterRoute;

    DBHelper dbHelper;
    SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final ListView routeList = binding.routeList;
        setRoute();
        routeList.setAdapter(adapterRoute);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), RoutPoints.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });

        return root;
    }
    public void connectDB(){
        dbHelper = new DBHelper(getContext());
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

    public void setRoute(){
        List<String> parks = new ArrayList<>();
        connectDB();
        Cursor cursor = db.rawQuery("SELECT * FROM parks", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            parks.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        adapterRoute = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, parks);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}