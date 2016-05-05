package com.example.tstoretest.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by 재화 on 2016-05-04.
 */
public class TstoreTestApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
