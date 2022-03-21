package com.over.parkulting.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.over.parkulting.R;
import com.over.parkulting.activity.ImageInfoActivity;
import com.over.parkulting.object.Picture;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pictureImageView;
        private Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.mContext = context;
            pictureImageView = itemView.findViewById(R.id.iv_picture);
        }

        public void bind(Picture item) {
            pictureImageView.setImageDrawable(Drawable.createFromPath(item.getPath()));
            pictureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ImageInfoActivity.class);
                    intent.putExtra("title", item.getType());
                    intent.putExtra("file", item.getPath());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private List<Picture> mData;
    private Context lContext;

    public GalleryAdapter(Context context) {
        this.lContext = context;
        this.mData = new ArrayList<>();;
    }

    public void addItem(Picture item) {
        mData.add(item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_picture, parent, false);
        return new ViewHolder(lContext, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
