package com.over.parkulting.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.over.parkulting.R;
import com.over.parkulting.adapter.PointAdapter;
import com.over.parkulting.object.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class AchievementsFragment extends Fragment {

    RecyclerView rv_achiev;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_achievement, container, false);
        rv_achiev = root.findViewById(R.id.rv_achiev);
        List<GeoPoint> pointsl = new ArrayList<>();

        pointsl.add(new GeoPoint("ytxnj", 12.99, 12.99, true));
        pointsl.add(new GeoPoint("ytxnj", 12.99, 12.99, false));

        PointAdapter adapter = new PointAdapter(getContext(), pointsl);
        rv_achiev.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_achiev.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}