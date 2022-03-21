package com.over.parkulting.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.over.parkulting.R;
import com.over.parkulting.object.GeoPoint;

import java.util.List;

public class PointAdapter extends BaseAdapter {
    private Context mContext;
    private List<GeoPoint> itemList;

    public PointAdapter(Context context, List<GeoPoint> list) {
        this.mContext = context;
        this.itemList = list;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public GeoPoint getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).
                    inflate(R.layout.item_point, viewGroup, false);
        }
        TextView name = view.findViewById(R.id.point_name);
        name.setText(getItem(i).getName());

        CardView cv_ip = view.findViewById(R.id.cv_ip);
        cv_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geoURI = "geo:"+Double.toString(getItem(i).getD())+","+Double.toString(getItem(i).getH())+"?z=15";
                Uri geo = Uri.parse(geoURI);
                Intent geoIntent = new Intent(Intent.ACTION_VIEW, geo);
                mContext.startActivity(geoIntent);
            }
        });


        return view;
    }
}
