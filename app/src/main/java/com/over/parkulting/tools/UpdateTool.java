package com.over.parkulting.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.over.parkulting.R;

public class UpdateTool {
    public static void allUpdate(Context context, String newVersion){
        Log.i("TAG", "allUpdate: update db and model");
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor e = getPrefs.edit();
        e.putString(context.getString(R.string.SHPREF_VER), newVersion);
        e.apply();
    }
}
