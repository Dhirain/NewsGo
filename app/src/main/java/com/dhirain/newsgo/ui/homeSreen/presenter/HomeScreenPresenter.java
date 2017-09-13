package com.dhirain.newsgo.ui.homeSreen.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dhirain.newsgo.database.NewsGoDatabaseHelper;
import com.dhirain.newsgo.database.manager.DBManager;
import com.dhirain.newsgo.model.FilterModel;
import com.dhirain.newsgo.model.KeyValueModel;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.model.comparator.AscendingDateComparator;
import com.dhirain.newsgo.model.comparator.DescendingDateComparator;
import com.dhirain.newsgo.network.NewsService;
import com.dhirain.newsgo.ui.homeSreen.view.HomeScreenView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by DJ on 14-09-2017.
 */

public class HomeScreenPresenter {


    private static final String TAG = "HomeScreenPresenter";
    private HomeScreenView homeScreenView;
    private Context context;
    private List<News> unchangesNewsList;
    private List<News> totalNewsList;
    private List<News> currentNewsList;
    private int currentPage = 0;
    private NewsGoDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private FilterModel filterModel;
    private boolean isDescending = false;

    public HomeScreenPresenter(HomeScreenView homeScreenView, Context context) {
        this.homeScreenView = homeScreenView;
        this.context = context;
        dbHelper = new NewsGoDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();


    }

    public void onViewAttached() {
        homeScreenView.showProgress();
        News firstNews = cupboard().withDatabase(db).query(News.class).get();
        if (firstNews != null) {
            Log.d(TAG, "onViewAttached: from DB");
            fetchRepoFromDb();
        } else {
            Log.d(TAG, "onViewAttached: from network");
            fetchRepoFromNetworkOrCache();
        }

    }

    private void fetchRepoFromDb() {
        totalNewsList = DBManager.instance().getFromDb();
        Log.d(TAG, "fetchRepoFromDb: " + totalNewsList.toString());
        unchangesNewsList = totalNewsList;
        homeScreenView.hideProgress();
        paggination();
        //networkCall for updating changes
    }

    //fetch List of repo  by using retrofit
    private void fetchRepoFromNetworkOrCache() {
        final Call<List<News>> repos = NewsService.instance().getAllNews();
        repos.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                if (response.isSuccessful()) {
                    totalNewsList = response.body();
                    unchangesNewsList = totalNewsList;
                    paggination();
                    new StoringAsync().execute(totalNewsList);
                    homeScreenView.hideProgress();

                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                t.printStackTrace();
                homeScreenView.hideProgress();
                homeScreenView.showNetworkFail();
            }
        });
    }

    public void paggination() {
        sortByTime(isDescending);
        if (currentNewsList == null) {
            currentPage = 0;
            currentNewsList = new ArrayList<>();
        }
        int tillPage = currentPage + 20;
        while (currentPage < tillPage && currentPage < totalNewsList.size()) {
            currentNewsList.add(totalNewsList.get(currentPage));
            currentPage++;
        }
        Log.d(TAG, "paggination: " + currentPage);
        homeScreenView.updateList(currentNewsList);
        /*currentNewsList = totalNewsList;
        homeScreenView.updateList(totalNewsList);*/
    }

    public void search(String query) {
        query = query.toLowerCase();

        final List<News> searchModelList = new ArrayList<>();
        for (News model : totalNewsList) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query.toLowerCase())) {
                searchModelList.add(model);
            }
        }
        homeScreenView.updateList(searchModelList);

    }

    public FilterModel getFilterModel(){
        if(filterModel==null){
            HashSet<String> setCategory = new HashSet<String>();
            HashSet<String> setPublisher = new HashSet<String>();
            for (News model : totalNewsList) {
                if (!setCategory.contains(model.getCatogery())) {
                    setCategory.add(model.getCatogery());
                }
                if (!setPublisher.contains(model.getPublisher())) {
                    setPublisher.add(model.getPublisher());
                }
            }
            ArrayList<KeyValueModel> categoryKeyValueModels = new ArrayList<KeyValueModel>();
            for(String cat:setCategory){
                categoryKeyValueModels.add(new KeyValueModel(cat));
            }

            ArrayList<KeyValueModel> publisherKeyValueModels = new ArrayList<KeyValueModel>();
            for(String pub:setPublisher){
                publisherKeyValueModels.add(new KeyValueModel(pub));
            }
            filterModel=new FilterModel(isDescending,categoryKeyValueModels,publisherKeyValueModels);
        }
        return filterModel;
    }


    public void applyFilter(FilterModel filterModel) {
        currentNewsList = null;
        isDescending = filterModel.isDescending();
        if (isAnyFilterApplied(filterModel)) {
            this.filterModel = filterModel;

            HashSet<String> catSet = creatFilterResultHashSet(filterModel.getCatogeryKeyValue());
            HashSet<String> pubSet = creatFilterResultHashSet(filterModel.getPublisherKeyValue());

            List<News> resultList = new ArrayList<>();
            for (News model : unchangesNewsList) {
                if ((catSet.contains(model.getCatogery()) && pubSet.contains(model.getPublisher()))) {
                    Log.d(TAG, "filterBackers: " + model.toString());
                    resultList.add(model);
                }
            }
            totalNewsList = resultList;
            if(resultList.isEmpty()){
                homeScreenView.showEmptyFilterState();
            }
            paggination();
        } else {
            homeScreenView.showSnackMessage("No Category or Publisher set ");
            this.filterModel = null;
            totalNewsList = unchangesNewsList;
            paggination();
        }
    }

    private boolean isAnyFilterApplied(FilterModel filterModel) {
        for (KeyValueModel cat :
                filterModel.getCatogeryKeyValue()) {
            if (cat.isChecked()){
                return true;
            }
        }

        for (KeyValueModel pub :
                filterModel.getPublisherKeyValue()) {
            if (pub.isChecked()){
                return true;
            }
        }
        return false;
    }

    private HashSet creatFilterResultHashSet(List<KeyValueModel> list){
        HashSet<String> allCatSet = new HashSet<String>();
        HashSet<String> catSet = new HashSet<String>();
        for (KeyValueModel cat :
                list) {
            allCatSet.add(cat.getKey());
            if (cat.isChecked()) {
                catSet.add(cat.getKey());
            }
        }
        if(catSet.isEmpty()){
            catSet = allCatSet;
        }
        return catSet;
    }

    public void sortByTime(boolean descending) {
        if(descending)
        Collections.sort(totalNewsList, new DescendingDateComparator());
        else
            Collections.sort(totalNewsList, new AscendingDateComparator());
    }



    public void applyFav(boolean isFavChecked) {
        homeScreenView.showProgress();
        if(isFavChecked){
            totalNewsList = DBManager.instance().getFavFromDb();

        }else {
            this.filterModel = null;
            unchangesNewsList = DBManager.instance().getFromDb();
            totalNewsList = unchangesNewsList;
        }
        homeScreenView.hideProgress();
        currentNewsList = null;
        paggination();
    }

    private class StoringAsync extends AsyncTask<List<News>, Void, String> {

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, "storing done", Toast.LENGTH_SHORT).show();
            // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected String doInBackground(List<News>... lists) {
            List<News> allNews = lists[0];
            News [] newsArray = new News[allNews.size()];
            newsArray = allNews.toArray(newsArray);
            for(News n: newsArray){
                cupboard().withDatabase(db).put(n);
            }

            return "done";
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}
