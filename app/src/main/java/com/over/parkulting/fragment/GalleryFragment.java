package com.over.parkulting.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.over.parkulting.R;
import com.over.parkulting.adapter.GalleryAdapter;
import com.over.parkulting.object.Picture;
import com.over.parkulting.tools.permission.PermissionCallback;
import com.over.parkulting.tools.permission.PermissionTool;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GalleryFragment extends Fragment implements PermissionCallback {

    private GalleryAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView rv_gallery = root.findViewById(R.id.rv_gallery);
        rv_gallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new GalleryAdapter(getContext());
        rv_gallery.setAdapter(adapter);

        new PermissionTool(getContext(), this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE });
        return root;
    }

    @Override
    public void onPermissionGranted() {
        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +
                        "/Park");
        List<File> files = Arrays.asList(dir.listFiles());
        Collections.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
        Collections.reverse(files);
        for (File filePhoto : files) adapter.addItem(new Picture(filePhoto));
    }

    @Override
    public void onPermissionDenied() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}