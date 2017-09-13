package com.dhirain.newsgo.ui.homeSreen.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.model.FilterModel;
import com.dhirain.newsgo.permision.PermissionProviderImpl;
import com.dhirain.newsgo.ui.filterScreen.activity.FilterActivity;
import com.dhirain.newsgo.ui.offlineCarousal.activity.CarousalActivity;
import com.dhirain.newsgo.ui.homeSreen.fragment.ListNewsFragment;

public class HomeScreenActivity extends AppCompatActivity {
    public static final int FILTER_REQUEST_CODE = 101;
    private ListNewsFragment listNewsFragment;
    FloatingActionButton Filterfab;
    private boolean isFavChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        checkPermision();

        setUI();

        showFragment();

        clickListner();

    }

    private void clickListner() {
        Filterfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Show filter Screen", Snackbar.LENGTH_LONG..show();
                listNewsFragment.showFilterScreen();

            }
        });
    }



    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Filterfab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
    }

    private void showFragment() {
        listNewsFragment = ListNewsFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.listContainer, listNewsFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home_screen, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Give query to presenter
                    listNewsFragment.search(newText);
                    return false;
                }
            });
        }
        MenuItem fav = menu.findItem(R.id.action_fav);
        fav.setChecked(isFavChecked);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_offline) {
            Intent intent = new Intent(this, CarousalActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_fav) {
            isFavChecked = !item.isChecked();
            item.setChecked(isFavChecked);
            item.setIcon(isFavChecked? R.drawable.fav: R.drawable.unfav);
            listNewsFragment.applyFav(isFavChecked);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILTER_REQUEST_CODE && resultCode==RESULT_OK){
            if (data!=null) {
                FilterModel filterModel = data.getParcelableExtra(FilterActivity.FILTER_MODEL);
                listNewsFragment.applyFilter(filterModel);
            }
        }
    }

    public void checkPermision(){
        PermissionProviderImpl provider = new PermissionProviderImpl(this);
        if(!provider.hasWriteExternalStoragePermission()){
            provider.requestWriteExternalStoragePermission();
        }

    }
}
