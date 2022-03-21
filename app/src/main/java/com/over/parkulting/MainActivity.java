package com.over.parkulting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.controls.templates.ThumbnailTemplate;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.over.parkulting.databinding.ActivityMainBinding;
import com.over.parkulting.ui.ImageFragment;
import com.over.parkulting.ui.RoutPoints;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    final int CAMERA_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap image = extras.getParcelable("data");
                String attractions = ImportIris.classifyImage(image, getApplicationContext());
                String folder ="";
                try {
                    folder = SavePicture(image, "Park", attractions);
                } catch (IOException e) {
                }
                Toast toast = Toast.makeText(this, attractions,Toast.LENGTH_LONG);
                toast.show();
                if (!attractions.equals("Не распознано")){
                    Intent intent = new Intent(this, ImageFragment.class);
                    intent.putExtra("title", attractions);
                    intent.putExtra("file", folder);
                    startActivity(intent);
                }
            }
        }
    }
    private String SavePicture(Bitmap img, String folderToSave, String name) throws IOException {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(pictures+"/"+folderToSave);
        dir.mkdir();
        name = name+"_"+System.currentTimeMillis()+".jpg";
        File file = new File(dir,name);
        FileOutputStream fos = new FileOutputStream(file);
        img.compress(Bitmap.CompressFormat.JPEG, 85, fos);
        fos.flush();
        fos.close();
        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(),  file.getName());
        return pictures+"/"+folderToSave+"/"+name;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}