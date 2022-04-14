package com.over.parkulting;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.over.parkulting.activity.Intro;
import com.over.parkulting.pojo.VersionPojo;
import com.over.parkulting.tools.ApiTool;
import com.over.parkulting.tools.DBHelper;
import com.over.parkulting.tools.Iris;
import com.over.parkulting.tools.UpdateTool;
import com.over.parkulting.tools.VersionTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        ApiTool.getInstance().getApi().getVersion().enqueue(new Callback<VersionPojo>() {
            @Override
            public void onResponse(Call<VersionPojo> call, Response<VersionPojo> response) {
                VersionPojo res = response.body();
                int compVer = VersionTool.comparisonVersion(
                        getPrefs.getString(getString(R.string.SHPREF_VER), "0"),
                        res.getVersion());

                if (compVer<0) {
                    ApiTool.getInstance().getApi().downloadParKultingFile().enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                File file = new File(DBHelper.getDatabasePath(getApplicationContext()), DBHelper.DATABASE_NAME);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(response.body().bytes());

                                ApiTool.getInstance().getApi().downloadModelFile().enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        try {
                                            File file2 = new File(Iris.getModelPath(getApplicationContext()), Iris.MODEL_NAME);
                                            FileOutputStream fileOutputStreamModel = new FileOutputStream(file2);
                                            fileOutputStreamModel.write(response.body().bytes());
                                            UpdateTool.allUpdate(getApplicationContext(), res.getVersion());
                                        } catch (IOException e) {}

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            catch (Exception ex){
                                Log.i("TAG", "onResponse: error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    Toast.makeText(getApplicationContext(), "there are updates", Toast.LENGTH_LONG).show();
                } else if (compVer>=0 ) {
                    Toast.makeText(getApplicationContext(), "no updates", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VersionPojo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.fail_chack_vers, Toast.LENGTH_LONG).show();
            }
        });


        boolean isFirstStart = getPrefs.getBoolean(getString(R.string.SHPREF_FSTART), true);
        if (isFirstStart) {
            Intent i = new Intent(this, Intro.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean(getString(R.string.SHPREF_FSTART), false);
            e.apply();
        }
    }
}
