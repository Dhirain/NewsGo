package com.dhirain.newsgo.ui.offlineCarousal.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.database.manager.DBManager;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.ui.offlineCarousal.adapter.CarousalAdapter;

import java.util.List;

/**
 * Created by DJ on 14-09-2017.
 */

public class CarousalActivity extends AppCompatActivity {
    private static final String TAG = "CarousalActivity";
    private ProgressDialog mProgressDialog;
    private ViewPager viewPager;
    private List<News> totalNewsList;
    private CarousalAdapter adapter;
    private ImageView imageView,leftSide,rightSide;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousal);
        setUI();
        setTitleWithBackPress("Offline Mode");
        intProgressbar();
        fetchRepoFromDb();
        handler = new Handler(getMainLooper());
        initAdapter();
        clickListener();
    }



    private void fetchRepoFromDb() {
        mProgressDialog.show();
        totalNewsList = DBManager.instance().getFavFromDb();
        Log.d(TAG, "fetchRepoFromDb: " + totalNewsList.toString());
        //mProgressDialog.dismiss();
    }

    private void setUI() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imageView =(ImageView) findViewById(R.id.no_result_found);
        leftSide = (ImageView) findViewById(R.id.left_side);
        rightSide = (ImageView) findViewById(R.id.right_side);
    }


    private void clickListener() {
        leftSide.setOnClickListener(view -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1));

        rightSide.setOnClickListener(view -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));
    }


    private void initAdapter() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(totalNewsList!=null && !totalNewsList.isEmpty()) {
                    viewPager.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    adapter = new CarousalAdapter(CarousalActivity.this, totalNewsList);
                    viewPager.setOffscreenPageLimit(2);
                    viewPager.setAdapter(adapter);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.GONE);
                    leftSide.setVisibility(View.GONE);
                    rightSide.setVisibility(View.GONE);
                }
                mProgressDialog.dismiss();
            }
        });


    }

    public void setTitleWithBackPress(String title) {
        final ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
        ActionBar mActionBar = getSupportActionBar();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(17, 17, 17));
        }

        if (mActionBar != null) {
            //mActionBar.setHomeAsUpIndicator(R.drawable.back);
            mActionBar.setBackgroundDrawable(cd);
            mActionBar.setTitle(title);
            mActionBar.setDisplayHomeAsUpEnabled(true); //to activate back pressed on home button press
            mActionBar.setDisplayShowHomeEnabled(false); //
        }

    }

    private void intProgressbar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
