package com.dhirain.newsgo.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dhirain.newsgo.NewsGoApp;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class NetworkUtills {

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) NewsGoApp.singleton().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
