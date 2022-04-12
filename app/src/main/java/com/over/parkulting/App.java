package com.over.parkulting;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.over.parkulting.activity.Intro;
import com.over.parkulting.pojo.VersionPojo;
import com.over.parkulting.tools.ApiTool;

import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private final String SHPH_FS = "FIRST_START";
    private final String SHPH_VERS = "VERSION";

    private int comparisonVersion(String ver1, String ver2){
        Scanner s1 = new Scanner(ver1);
        Scanner s2 = new Scanner(ver2);
        s1.useDelimiter("\\.");
        s2.useDelimiter("\\.");

        while(s1.hasNextInt() && s2.hasNextInt()) {
            int v1 = s1.nextInt();
            int v2 = s2.nextInt();
            if(v1 < v2) {
                return -1;
            } else if(v1 > v2) {
                return 1;
            }
        }

        if(s1.hasNextInt()) return 1;
        return 0;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        ApiTool.getInstance().getApi().getVersion().enqueue(new Callback<VersionPojo>() {
            @Override
            public void onResponse(Call<VersionPojo> call, Response<VersionPojo> response) {
                VersionPojo res = response.body();
                int compVer = comparisonVersion(getPrefs.getString(SHPH_VERS, "0"),
                        res.getVersion());

                if (compVer<0) {
                    Toast.makeText(getApplicationContext(), "there are updates", Toast.LENGTH_LONG).show();
                } else if ( compVer==0 || compVer>0 ) {
                    Toast.makeText(getApplicationContext(), "no updates", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VersionPojo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.fail_chack_vers, Toast.LENGTH_LONG).show();
            }
        });

        boolean isFirstStart = getPrefs.getBoolean(SHPH_FS, true);
        if (isFirstStart) {
            Intent i = new Intent(this, Intro.class);

            startActivity(i);

            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean(SHPH_FS, false);
            e.apply();
        }
    }
}
