package com.dhirain.newsgo.ui.filterScreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.model.FilterModel;
import com.dhirain.newsgo.model.KeyValueModel;
import com.dhirain.newsgo.ui.filterScreen.adapter.FilterListAdapter;
import com.dhirain.newsgo.ui.filterScreen.callback.CheckListner;
import com.dhirain.newsgo.ui.filterScreen.view.FilterScreenView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by DJ on 14-09-2017.
 */

public class FilterActivity extends AppCompatActivity implements FilterScreenView,View.OnClickListener,CheckListner{
    private static final String TAG = "FilterActivity";
    private FilterModel mFilterModel;
    private boolean isAscending = false;
    private ArrayList<KeyValueModel> mPublisherKeyValueList;
    private ArrayList<KeyValueModel> mCatogeryKeyValueList;
    private TextView sortTV, publisherTV, categoryTV,clearAll,ApplyAll;
    private RecyclerView listView;
    private LinearLayout sortLL;
    private RadioButton ascRB,descRB;
    private FilterListAdapter adapter;

    public static final String FILTER_MODEL = "filter_model";

    public static void show(Activity activity, FilterModel filterModel, int filterRequestCode) {
        Intent intent = new Intent(activity, FilterActivity.class);
        intent.putExtra(FILTER_MODEL, filterModel);
        activity.startActivityForResult(intent,filterRequestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_screen);
        getFilterModelFromIntent();
        setTitleWithBackPress("Filter");
        setUI();
        SetClickListner();
        initAdapter();
        showSort();

    }

    private void setUI() {
        sortTV = (TextView) findViewById(R.id.sortTV);
        publisherTV = (TextView) findViewById(R.id.publisherTV);
        categoryTV = (TextView) findViewById(R.id.categoryTV);
        clearAll = (TextView) findViewById(R.id.clearAll);
        ApplyAll = (TextView) findViewById(R.id.applyAll);
        listView = (RecyclerView) findViewById(R.id.catogery_publisher_listview);
        sortLL = (LinearLayout) findViewById(R.id.sort_ll);
        descRB = (RadioButton) findViewById(R.id.desc_RB);
        ascRB = (RadioButton) findViewById(R.id.asc_RB);
    }

    private void SetClickListner() {
        descRB.setOnClickListener(this);
        ascRB.setOnClickListener(this);
        sortTV.setOnClickListener(this);
        clearAll.setOnClickListener(this);
        publisherTV.setOnClickListener(this);
        categoryTV.setOnClickListener(this);
        ApplyAll.setOnClickListener(this);
    }

    private void initAdapter() {
        listView.setHasFixedSize(true);
        adapter = new FilterListAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(manager);
        listView.setAdapter(adapter);
    }


    private void getFilterModelFromIntent() {
        mFilterModel = getIntent().getParcelableExtra(FILTER_MODEL);
        //Log.d(TAG, "getFilterModelFromIntent: "+mFilterModel.toString());
        isAscending = mFilterModel.isDescending();
        mPublisherKeyValueList = mFilterModel.getPublisherKeyValue();
        mCatogeryKeyValueList = mFilterModel.getCatogeryKeyValue();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.desc_RB:
                isAscending = false;
                break;

            case R.id.asc_RB:
                isAscending = true;
                break;

            case R.id.sortTV:
                showSort();
                break;

            case R.id.categoryTV:
                showCategoryFilter();
                break;

            case R.id.publisherTV:
                showPublisherFilter();
                break;

            case R.id.clearAll:
                clearAllFilter();
                break;

            case R.id.applyAll:
                ApplyFilter();
                break;
        }
    }

    @Override
    public void makeAllInvisible() {
        sortTV.setBackgroundColor(getResources().getColor(R.color.background_grey));
        publisherTV.setBackgroundColor(getResources().getColor(R.color.background_grey));
        categoryTV.setBackgroundColor(getResources().getColor(R.color.background_grey));
        listView.setVisibility(View.INVISIBLE);
        sortLL.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSort() {
        makeAllInvisible();
        sortTV.setBackgroundColor(getResources().getColor(R.color.white));

        sortLL.setVisibility(View.VISIBLE);
        if (isAscending) {
            ascRB.setChecked(true);
        }
        else {
            descRB.setChecked(true);
        }
    }

    @Override
    public void showPublisherFilter() {
        makeAllInvisible();
        publisherTV.setBackgroundColor(getResources().getColor(R.color.white));
        listView.setVisibility(View.VISIBLE);
        adapter.updateList(mPublisherKeyValueList,true);
    }

    @Override
    public void showCategoryFilter() {
        makeAllInvisible();
        categoryTV.setBackgroundColor(getResources().getColor(R.color.white));
        listView.setVisibility(View.VISIBLE);
        adapter.updateList(mCatogeryKeyValueList,false);
    }

    @Override
    public void clearAllFilter() {
        isAscending = false;
        clearCategory();
        clearPublisher();
        showSort();


    }

    private void clearCategory() {
        Iterator<KeyValueModel> iterator = mCatogeryKeyValueList.iterator();
        while (iterator.hasNext()) {
            KeyValueModel current = iterator.next();
            current.setChecked(false);
        }
    }

    private void clearPublisher() {
        Iterator<KeyValueModel> iterator = mPublisherKeyValueList.iterator();
        while (iterator.hasNext()) {
            KeyValueModel current = iterator.next();
            current.setChecked(false);
        }
    }

    @Override
    public void ApplyFilter() {
        System.out.println(mCatogeryKeyValueList.toString());
        System.out.println(mPublisherKeyValueList.toString());
        System.out.println(isAscending);

        Intent intent = new Intent();
        intent.putExtra(FILTER_MODEL, new FilterModel(isAscending,mCatogeryKeyValueList,mPublisherKeyValueList));
        setResult(RESULT_OK,intent);
        finish();
    }


    @Override
    public void updatePublisherList(String publisher, boolean to) {
        Iterator<KeyValueModel> iterator = mPublisherKeyValueList.iterator();
        while (iterator.hasNext()){
            KeyValueModel current = iterator.next();
            if(current.getKey().equalsIgnoreCase(publisher)){
                current.setChecked(to);
            }
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void updateCatogeryList(String catogery, boolean to) {
        Iterator<KeyValueModel> iterator = mCatogeryKeyValueList.iterator();
        while (iterator.hasNext()){
            KeyValueModel current = iterator.next();
            if(current.getKey().equalsIgnoreCase(catogery)){
                current.setChecked(to);
            }
        }
    }
}
