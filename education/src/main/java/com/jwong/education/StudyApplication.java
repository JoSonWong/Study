package com.jwong.education;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.jwong.education.db.DbController;

public class StudyApplication extends Application {

    private static DbController dbController;

    @Override
    public void onCreate() {
        super.onCreate();
        dbController = DbController.getInstance(getApplicationContext());
    }

    public static DbController getDbController() {
        return dbController;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
