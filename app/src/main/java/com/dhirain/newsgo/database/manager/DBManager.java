package com.dhirain.newsgo.database.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dhirain.newsgo.NewsGoApp;
import com.dhirain.newsgo.database.NewsGoDatabaseHelper;
import com.dhirain.newsgo.model.News;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by DJ on 14-09-2017.
 */

public class DBManager {

    private static DBManager sSingleton;
    private  Context mContext;
    private NewsGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public static synchronized DBManager instance() {
        if (sSingleton == null) {
            sSingleton = new DBManager();
        }

        return sSingleton;
    }

    private DBManager() {
        mContext= NewsGoApp.singleton().getContext();
        dbHelper= new NewsGoDatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
    }

    public List<News> getFromDb() {
        List<News> repos = new ArrayList<>();
        QueryResultIterable<News> itr = cupboard().withDatabase(db).query(News.class).query();
        Log.d("Database", "From db");
        for (News s : itr)
            repos.add(s);
        return repos;
    }

    public List<News> getFavFromDb() {
        List<News> repos = new ArrayList<>();
        QueryResultIterable<News> itr = cupboard().
                withDatabase(db).
                query(News.class)
                .withSelection("isFavorite = ?", Integer.toString(1))
                .query();
        Log.d("Database", "From fav db");
        for (News s : itr)
            repos.add(s);
        return repos;
    }

    public int updateFavToItem(int s_no,boolean updateToValue){
        ContentValues values = new ContentValues(1);
        values.put("isFavorite", updateToValue);
        return cupboard()
                .withDatabase(db)
                .update(News.class, values, "s_no = ?", String.valueOf(s_no));
    }

}
