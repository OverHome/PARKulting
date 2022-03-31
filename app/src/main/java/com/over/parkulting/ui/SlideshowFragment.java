package com.over.parkulting.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.over.parkulting.tools.DBHelper;
import com.over.parkulting.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final ListView list = binding.list;
        db = DBHelper.connectDB(getContext());
        List<String> tpoints = new ArrayList<>();
        Cursor userCursor = db.rawQuery("SELECT * FROM user_points where is_take = 1" , null);
        Cursor pointCursor;
        userCursor.moveToFirst();
        while (!userCursor.isAfterLast()) {
            pointCursor = db.rawQuery("SELECT * FROM points_in_park where id = " + userCursor.getInt(0), null);
            pointCursor.moveToFirst();
            tpoints.add(pointCursor.getString(2));
            userCursor.moveToNext();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,tpoints);
        list.setAdapter(arrayAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}