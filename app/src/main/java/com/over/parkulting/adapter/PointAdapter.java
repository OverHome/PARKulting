package com.over.parkulting.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.over.parkulting.R;
import com.over.parkulting.activity.ImageInfoActivity;
import com.over.parkulting.object.GeoPoint;
import com.over.parkulting.object.Picture;

import java.util.List;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pointTextView;
        private CardView cv_ip;
        private Context mContext;
        private ImageView imageCheck;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.mContext = context;
            pointTextView = itemView.findViewById(R.id.point_name);
            cv_ip = itemView.findViewById(R.id.cv_ip);
            imageCheck = itemView.findViewById(R.id.imageCheck);
        }

        public void bind(GeoPoint item) {
            pointTextView.setText(item.getName());

            if (item.isPosit()) imageCheck.setVisibility(View.VISIBLE);
            else imageCheck.setVisibility(View.INVISIBLE);

            cv_ip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String geoURI = "geo:"+Double.toString(item.getD())+","+Double.toString(item.getH())+"?z=15";
                    Uri geo = Uri.parse(geoURI);
                    Intent geoIntent = new Intent(Intent.ACTION_VIEW, geo);
                    mContext.startActivity(geoIntent);
                }
            });
        }
    }

    private Context mContext;
    private List<GeoPoint> itemList;

    public PointAdapter(Context context, List<GeoPoint> list) {
        this.mContext = context;
        this.itemList = list;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public PointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_point, parent, false);
        return new PointAdapter.ViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointAdapter.ViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }
}
