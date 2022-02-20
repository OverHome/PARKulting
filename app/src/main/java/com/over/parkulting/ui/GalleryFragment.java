package com.over.parkulting.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.over.parkulting.R;
import com.over.parkulting.databinding.FragmentGalleryBinding;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getPhotos();

        return root;
    }

    private void getPhotos(){
        List<ImageView> photoViewsList = new ArrayList<>();
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(dcim+"/"+"Park");
        File directory = new File(dir.toString());
        File[] files = directory.listFiles();
        List<File> f = Arrays.asList(files);
        Collections.sort(f, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.compare(f1.lastModified(), f2.lastModified());
            }
        });
        f.toArray(files);

        LinearLayout layout = binding.layout;

        for (int i = 0; i < (int)Math.ceil(files.length/3.0); i++)
        {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.line_galary, null, false);
            photoViewsList.add(view.findViewById(R.id.photoView1));
            photoViewsList.add(view.findViewById(R.id.photoView2));
            photoViewsList.add(view.findViewById(R.id.photoView3));
            layout.addView(view);
        }
        ImageView[] photoViews = new ImageView[photoViewsList.size()];
        photoViewsList.toArray(photoViews);
        View.OnClickListener onClickListenerPhoto = view1 -> {
            Intent intent = new Intent(getContext(), ImageFragment.class);
            intent.putExtra("title", files[view1.getId()].getName().split("_")[0]);
            intent.putExtra("file", files[view1.getId()].getAbsolutePath());
            startActivity(intent);
        };
        for (int i = 0; i < files.length; i++)
        {
            photoViews[files.length-i-1].setImageDrawable(Drawable.createFromPath(files[i].getAbsolutePath()));
            photoViews[files.length-i-1].setOnClickListener(onClickListenerPhoto);
            photoViews[files.length-i-1].setId(i);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}