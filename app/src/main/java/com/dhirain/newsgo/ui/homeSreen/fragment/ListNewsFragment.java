package com.dhirain.newsgo.ui.homeSreen.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.model.FilterModel;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.ui.filterScreen.activity.FilterActivity;
import com.dhirain.newsgo.ui.homeSreen.activity.HomeScreenActivity;
import com.dhirain.newsgo.ui.homeSreen.adapter.ListAdapter;
import com.dhirain.newsgo.ui.homeSreen.helper.SwipeTouchHelper;
import com.dhirain.newsgo.ui.homeSreen.presenter.HomeScreenPresenter;
import com.dhirain.newsgo.ui.homeSreen.view.HomeScreenView;
import com.dhirain.newsgo.utills.EndlessRecyclerViewScrollListener;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by DJ on 14-09-2017.
 */

public class ListNewsFragment extends Fragment implements HomeScreenView {
    private ListAdapter adapter;
    private ProgressDialog mProgressDialog;
    private View view;
    private RecyclerView recyclerView;
    private HomeScreenPresenter screenPresenter;

    public static ListNewsFragment newInstance() {
        ListNewsFragment listNewsFragment = new ListNewsFragment();
        return listNewsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_list, container, false);
        setUI(view);
        intProgressbar();
        initAdapter();
        setPresenter();
        return view;

    }

    private void setUI(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        showListState();
    }

    //initialize Addapter
    private void initAdapter() {
        recyclerView.setHasFixedSize(true);
        adapter = new ListAdapter(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new mVideoList to the bottom of the list
                Log.d(TAG, "onLoadMore: paggination");
                screenPresenter.paggination();
                //Toast.makeText(getContext(), "Adding items", Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        ItemTouchHelper.Callback mCallback = new SwipeTouchHelper(adapter);
        ItemTouchHelper swipeTouchHelper= new ItemTouchHelper(mCallback);
        swipeTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void intProgressbar() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    //set presenter
    private void setPresenter() {
        screenPresenter = new HomeScreenPresenter(this, getContext());
        screenPresenter.onViewAttached();
    }

    @Override
    public void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showNetworkFail() {
        Snackbar.make(view, "Network failure Opps :-(", Snackbar.LENGTH_LONG);

    }

    @Override
    public void updateList(List<News> totalNewsList) {
        adapter.updateList(totalNewsList);
    }


    public void search(String searchText) {
        screenPresenter.search(searchText);
    }

    @Override
    public void showFilterScreen() {
        FilterModel filterModel= screenPresenter.getFilterModel();
        FilterActivity.show(getActivity(),filterModel, HomeScreenActivity.FILTER_REQUEST_CODE);
    }

    @Override
    public void showSnackMessage(String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyFilterState() {
        ImageView imageView = (ImageView) view.findViewById(R.id.no_result_found);
        imageView.setVisibility(View.VISIBLE);

    }

    @Override
    public void showListState() {
        ImageView imageView = (ImageView) view.findViewById(R.id.no_result_found);
        imageView.setVisibility(View.GONE);
    }


    public void applyFilter(FilterModel filterModel) {
        showListState();
        screenPresenter.applyFilter(filterModel);
    }

    public void applyFav(boolean isFavChecked) {
        Log.d(TAG, "applyFav: "+isFavChecked);
        screenPresenter.applyFav(isFavChecked);
    }
}

