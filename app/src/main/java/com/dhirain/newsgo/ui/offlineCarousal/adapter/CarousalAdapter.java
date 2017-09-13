package com.dhirain.newsgo.ui.offlineCarousal.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.fileUtils.FileUtil;
import com.dhirain.newsgo.fileUtils.ReadWrite;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.utills.GenericCallback;

import java.util.List;

/**
 * Created by DJ on 14-09-2017.
 */

public class CarousalAdapter extends PagerAdapter {
    private static final String TAG = "CarousalAdapter";
    private final String root;
    private Context context;
    private List<News> newsList;
    //private ProgressDialog progressDialog;

    public CarousalAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
        //this.progressDialog = mProgressDialog;
        this.root = Environment.getExternalStorageDirectory().toString();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.carousal_item, container, false);

        WebView webView = (WebView) viewItem.findViewById(R.id.carousal_webview);

        TextView textView = (TextView) viewItem.findViewById(R.id.carousal_textview);

        textView.setText("Loading File "+root + "/" + newsList.get(position).getS_no() + FileUtil.HTML);

        String fileName = String.valueOf(newsList.get(position).getS_no());

        Log.d(TAG, "instantiateItem: "+fileName);

        Log.d(TAG, "url: "+newsList.get(position).getUrl());

        //progressDialog.show();

        ReadWrite.readFile(fileName, new GenericCallback<String>() {
            @Override
            public void onRequestSuccess(String fileString) {
                Log.d(TAG, "onRequestSuccess: file read compelte");
                String readStringFromLocal = fileString;
                if(!readStringFromLocal.equalsIgnoreCase("")){
                    webView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.loadData(readStringFromLocal, "text/html", "UTF-8");
                }
                else{
                    webView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("error Loading "+root + "/" + newsList.get(position).getS_no() + FileUtil.HTML);
                }
            }

            @Override
            public void onRequestFailure(Throwable error, String errorMessage) {
                //webView.loadUrl(newsList.get(position).getUrl());
                Log.d(TAG, "onRequestFailure: ");
                error.printStackTrace();
                webView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                textView.setText("error Loading "+root + "/" + newsList.get(position).getS_no() + FileUtil.HTML);
            }
        });

        //progressDialog.hide();

        /*String readStringFromLocal = "";
        if (newsList.get(position).isFavorite()) {
            try {
                readStringFromLocal = ReadWrite.readFile(String.valueOf(newsList.get(position).getS_no()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
          /*if (!readStringFromLocal.equals("")) {
            webView.loadData(readStringFromLocal, "text/html", "UTF-8");
            Log.d(TAG, "showOnWeb: From Local");
        } else {
            Log.d(TAG, "showOnWeb: from Server");
            webView.loadUrl(newsList.get(position).getUrl());
        }*/
        container.addView(viewItem);

        return viewItem;
    }


    @Override
    public int getCount() {
        if (newsList == null)
            return 0;
        else {
            return newsList.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    /*private String readFile(int s_no){
        String readStringFromLocal = "";
        try {
        readStringFromLocal = ReadWrite.readFile(String.valueOf(s_no));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return readStringFromLocal;
    }*/

    private class MyBrowser extends WebViewClient {
        private WebView myBrowserWebView;
        private TextView myBrowsertTextView;
        //private ProgressDialog progressBar;


        public MyBrowser(WebView myBrowserWebView, TextView myBrowsertTextView) {
            this.myBrowserWebView = myBrowserWebView;
            this.myBrowsertTextView = myBrowsertTextView;
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
            //progressBar.dismiss();
            this.myBrowsertTextView.setVisibility(View.GONE);
            this.myBrowserWebView.setVisibility(View.VISIBLE);
        }
    }

}
