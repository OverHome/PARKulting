package com.over.parkulting.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.over.parkulting.R;
import com.over.parkulting.databinding.ActivityMasterBinding;
import com.over.parkulting.pojo.VersionPojo;
import com.over.parkulting.tools.ApiTool;
import com.over.parkulting.tools.PermissionTool;
import com.over.parkulting.tools.VersionTool;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                R.id.navigation_home,
                R.id.navigation_gallery,
                R.id.navigation_camera,
                R.id.navigation_notifications).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_master);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (pt != null) {
            pt.callBackPermissionResult(grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}