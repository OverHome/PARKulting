package com.over.parkulting.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.over.parkulting.R;
import com.over.parkulting.activity.ImageInfoActivity;
import com.over.parkulting.adapter.GalleryAdapter;
import com.over.parkulting.object.Picture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class GalleryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView rv_gallery = root.findViewById(R.id.rv_gallery);
        rv_gallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        GalleryAdapter adapter = new GalleryAdapter(getContext());

        //region getPhotos
        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+
                        "/"+"Park");
        File directory = new File(dir.toString());
        List<File> files = Arrays.asList(directory.listFiles());
        Collections.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

        for (File filePhoto : files) adapter.addItem(new Picture(filePhoto));
        //endregion

        rv_gallery.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}