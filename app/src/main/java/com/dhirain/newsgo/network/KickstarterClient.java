package com.dhirain.newsgo.network;

import com.dhirain.newsgo.model.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public interface KickstarterClient {
    @GET("/newsjson")
    Call<List<News>> getAllNews();
}
