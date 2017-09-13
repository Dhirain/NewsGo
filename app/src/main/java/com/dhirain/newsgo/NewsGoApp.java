package com.dhirain.newsgo;

import android.app.Application;
import android.content.Context;

/**
 * Created by DJ on 09-09-2017.
 */

public class NewsGoApp extends Application {

    private static NewsGoApp sSingleton;

    public static NewsGoApp singleton() {
        return sSingleton;
    }

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        sSingleton = this;
        context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }
}
