package com.dhirain.newsgo.network;

import com.dhirain.newsgo.NewsGoApp;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class NewsService {

    public static final String API_BASE_URL = "http://starlord.hackerearth.com/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static KickstarterClient builder;

    public static KickstarterClient instance() {
        return builder;
    }

    static {
        //setup cache
        httpClient.addNetworkInterceptor(new CacheInterceptor());
        httpClient.cache(setCacheSize(10 * 1024 * 1024));// 10 MB

        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(KickstarterClient.class);
    }

    private static Cache setCacheSize(int cacheSize) {
        File httpCacheDirectory = new File(NewsGoApp.singleton().getContext().getCacheDir(), "responses");
        return new Cache(httpCacheDirectory, cacheSize);
    }

}
