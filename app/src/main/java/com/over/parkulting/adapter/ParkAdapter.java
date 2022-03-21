package com.over.parkulting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.over.parkulting.R;
import com.over.parkulting.object.Park;

import java.util.ArrayList;

public class ParkAdapter extends ArrayAdapter<Park> {
    public ParkAdapter(Context context, ArrayList<Park> parks) {
        super(context, 0, parks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Park park = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_park, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_park_title);
        tvName.setText(park.getName());

        return convertView;
    }
}
