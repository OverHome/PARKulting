package com.over.parkulting.activity;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.over.parkulting.R;
import com.over.parkulting.databinding.ActivityMasterBinding;
import com.over.parkulting.tools.PermissionTool;

public class MasterActivity extends AppCompatActivity {

    private ActivityMasterBinding binding;
    public PermissionTool pt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Parkulting);
        super.onCreate(savedInstanceState);

        binding = ActivityMasterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_gallery, R.id.navigation_camera, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_master);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // TODO: доделать диалог загрузки
        /*Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_download);
        dialog.show();*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (pt != null) {
            pt.callBackPermissionResult(grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}