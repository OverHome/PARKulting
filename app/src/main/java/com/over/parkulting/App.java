package com.over.parkulting;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.over.parkulting.activity.Intro;

public class App extends Application {

    private final String SHPH_FS = "FIRST_START";
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

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
