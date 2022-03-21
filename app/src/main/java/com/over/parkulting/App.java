package com.over.parkulting;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: заменить на payload
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
    }
}
