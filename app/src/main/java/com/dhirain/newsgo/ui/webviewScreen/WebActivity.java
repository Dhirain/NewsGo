package com.dhirain.newsgo.ui.webviewScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.fileUtils.ReadWrite;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.utills.GenericCallback;

import java.io.IOException;

public class WebActivity extends AppCompatActivity {

    public static final String IMAGE_TRANSITION_NAME = "transitionText";
    public static final String KEY_NEWS = "key_news";
    private static final String TAG = "WebActivity";

    private News news;
    private TextView titleText;
    private WebView webView;
    private Toolbar toolbar;
    private ImageView imageView;
    private FloatingActionButton fab;
    //private ProgressDialog mProgressDialog;
    private ProgressBar progressBar;
    private boolean isFavChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        getIntentData();

        setUI();

        clickListner();

        showOnWeb();

        ViewCompat.setTransitionName(titleText , WebActivity.IMAGE_TRANSITION_NAME);
    }

    private void showOnWeb() {
        webView.setWebViewClient(new MyBrowser(progressBar,imageView,webView));
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if(news.isFavorite()) {
            ReadWrite.readFile(String.valueOf(news.getS_no()), new GenericCallback<String>() {
                @Override
                public void onRequestSuccess(String fileString) {
                    Log.d(TAG, "onRequestSuccess: file read compelte");
                    String readStringFromLocal = fileString;
                    if (!readStringFromLocal.equalsIgnoreCase("")) {
                        webView.setVisibility(View.VISIBLE);
                        webView.loadData(readStringFromLocal, "text/html", "UTF-8");
                    } else {
                        webView.loadUrl(news.getUrl());
                    }
                }

                @Override
                public void onRequestFailure(Throwable error, String errorMessage) {
                    //webView.loadUrl(newsList.get(position).getUrl());
                    Log.d(TAG, "onRequestFailure: ");
                    error.printStackTrace();
                    webView.loadUrl(news.getUrl());
                }
            });
        }
        else {
            Log.d(TAG, "showOnWeb: from Server");
            webView.loadUrl(news.getUrl());
        }
        //webView.loadUrl(news.getUrl());
       /* String readStringFromLocal = "";
        if (news.isFavorite()) {
            try {
                readStringFromLocal = ReadWrite.readFile(String.valueOf(news.getS_no()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!readStringFromLocal.equals("")) {
            webView.loadData(readStringFromLocal, "text/html", "UTF-8");
            Log.d(TAG, "showOnWeb: From Local");
        }
        else {
            Log.d(TAG, "showOnWeb: from Server");
            webView.loadUrl(news.getUrl());
        }*/
    }

    private void getIntentData() {
        news = getIntent().getParcelableExtra(KEY_NEWS);
    }


    private void clickListner() {
        fab.setOnClickListener(view -> {
            isFavChecked = !isFavChecked;
            fab.setImageResource(isFavChecked ? R.drawable.like : R.drawable.unfav);
            news.setFavorite(isFavChecked);
            if(isFavChecked) {
                try {
                    ReadWrite.saveFile(news.getUrl(), String.valueOf(news.getS_no()));
                    Snackbar.make(view, "Downloading your News", Snackbar.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void setUI() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        webView = (WebView) findViewById(R.id.webView);
        titleText = (TextView) findViewById(R.id.titleName);
        titleText.setText(news.getTitle());
        imageView = (ImageView) findViewById(R.id.no_result_found);
        isFavChecked = news.isFavorite();
        fab.setImageResource(isFavChecked ? R.drawable.like : R.drawable.unfav);
        progressBar = (ProgressBar)findViewById(R.id.progess);
        progressBar.setVisibility(View.VISIBLE);
    }

    /*private void intProgressbar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }*/

    public static void show(Context context,News news) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.KEY_NEWS, news);
        context.startActivity(intent);
    }

    private class MyBrowser extends WebViewClient {
        private ProgressBar progressBar;
        private ImageView imageView;
        private WebView webView;

        public MyBrowser(ProgressBar progressBar, ImageView imageView,WebView webView) {
            this.progressBar = progressBar;
            this.imageView = imageView;
            this.webView = webView;

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d(TAG, "onReceivedError: 404");
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }



        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }
}
