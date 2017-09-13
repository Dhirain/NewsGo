package com.dhirain.newsgo.ui.homeSreen.view;

import com.dhirain.newsgo.model.News;

import java.util.List;

/**
 * Created by DJ on 14-09-2017.
 */

public interface HomeScreenView {

    void showProgress();

    void hideProgress();

    void showNetworkFail();

    void updateList(List<News> totalNewsList);

    void showFilterScreen();

    void showSnackMessage(String s);

    void showEmptyFilterState();

    void showListState();
}
