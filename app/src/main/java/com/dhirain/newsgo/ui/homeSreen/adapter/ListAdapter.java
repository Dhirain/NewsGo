package com.dhirain.newsgo.ui.homeSreen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.fileUtils.ReadWrite;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.ui.homeSreen.view.ItemViewHolder;
import com.dhirain.newsgo.ui.webviewScreen.WebActivity;
import com.dhirain.newsgo.utills.CategoryUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private static final String TAG = "ListAdapter";
    private List<News> newsList;
    private Context context;
    News currentNews;
    private int lastPosition;

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<News> newRepo) {
        newsList = new ArrayList<>();
        this.newsList = newRepo;
        this.lastPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (newsList != null) {
            showCurrentItem(holder, newsList.get(position));
            //setAnimation(holder.parent,position);
        }
    }

    private void showCurrentItem(final ItemViewHolder holder, final News news) {
        holder.titleText.setText(news.getTitle());
        holder.url.setText(news.getUrl());
        holder.timestamp.setText(news.getDateInTimeStamp());
        holder.host.setText(news.getHostame());
        holder.publisher.setText(news.getPublisher());
        holder.catogery.setText(CategoryUtil.getCatogeryToDisplay(news.getCatogery()));
        holder.like.setImageResource(news.isFavorite() ? R.drawable.like: R.drawable.unlike);

        holder.like.setOnClickListener(view -> {
            news.setFavorite(!news.isFavorite());
            holder.like.setImageResource(news.isFavorite() ? R.drawable.like : R.drawable.unlike);
            if(news.isFavorite()){
                try {
                    ReadWrite.saveFile(news.getUrl(),Integer.toString(news.getS_no()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        holder.titleText.setOnClickListener(view -> showWebViewScreen(holder,news));
        holder.meta.setOnClickListener(view -> showWebViewScreen(holder,news));
    }

    private void showWebViewScreen(ItemViewHolder holder, News news) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                new Pair(holder.titleText, WebActivity.IMAGE_TRANSITION_NAME));
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.KEY_NEWS, news);
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }

    @Override
    public int getItemCount() {
        if (newsList == null)
            return 0;
        else {
            return newsList.size();
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void swap(int adapterPosition, int adapterPosition1) {
        Collections.swap(newsList, adapterPosition, adapterPosition1);
        notifyItemMoved(adapterPosition, adapterPosition1);
    }

    public void remove(int adapterPosition) {
        int id = newsList.get(adapterPosition).getS_no();
        newsList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }
}
