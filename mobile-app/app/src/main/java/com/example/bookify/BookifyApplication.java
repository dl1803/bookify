package com.example.bookify;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BookifyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
