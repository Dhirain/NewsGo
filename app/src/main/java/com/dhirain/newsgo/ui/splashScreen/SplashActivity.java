package com.dhirain.newsgo.ui.splashScreen;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.database.NewsGoDatabaseHelper;
import com.dhirain.newsgo.ui.homeSreen.activity.HomeScreenActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SplashActivity extends AppCompatActivity {
    private boolean isActivityVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getDb2();
        NewsGoDatabaseHelper dbHelper = new NewsGoDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityVisible = true;
        delayedHide(4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isActivityVisible) {
                        Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delayMillis);
    }

    private void getDb2() {

        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                Log.v("Splash", "sd2 can write");
                String currentDBPath = "/data/data/com.dhirain.newsgo/databases/newsgo.db";
                String backupDBPath = "checkthis.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    Log.v("Splash", "DB EXISTS2");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }
}
