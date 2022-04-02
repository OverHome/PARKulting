package com.over.parkulting.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.over.parkulting.R;
import com.over.parkulting.activity.MasterActivity;
import com.over.parkulting.adapter.GalleryAdapter;
import com.over.parkulting.object.Picture;
import com.over.parkulting.tools.permission.PermissionCallback;
import com.over.parkulting.tools.permission.PermissionTool;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GalleryFragment extends Fragment implements PermissionCallback {
    RecyclerView rv_gallery;
    ConstraintLayout permgallerdenine;
    private GalleryAdapter adapter;
    private PermissionTool permissionTool;
    private final String[] permissionlist = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        rv_gallery = root.findViewById(R.id.rv_gallery);
        permgallerdenine = root.findViewById(R.id.permgallerydenine);
        Button btnRegGallery = root.findViewById(R.id.btn_reg_gallery);
        rv_gallery.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter = new GalleryAdapter(getContext());
        rv_gallery.setAdapter(adapter);

        permissionTool = new PermissionTool(getContext(), this, permissionlist);
        btnRegGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionTool.check();
            }
        });

        permissionTool.check();
        return root;
    }

    @Override
    public void onPermissionGranted() {
        permgallerdenine.setVisibility(View.INVISIBLE);
        rv_gallery.setVisibility(View.VISIBLE);
        rv_gallery.setAdapter(adapter);

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
        ((MasterActivity) getActivity()).pt = permissionTool;
        permissionTool.reqPerm(getActivity(), permissionlist);
    }

    @Override
    public void onPermissionDeniedByTheUser() {
        rv_gallery.setVisibility(View.INVISIBLE);
        permgallerdenine.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}