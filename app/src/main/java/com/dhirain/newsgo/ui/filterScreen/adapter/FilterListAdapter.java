package com.dhirain.newsgo.ui.filterScreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import com.dhirain.newsgo.R;
import com.dhirain.newsgo.model.KeyValueModel;
import com.dhirain.newsgo.model.News;
import com.dhirain.newsgo.ui.filterScreen.callback.CheckListner;

import java.util.ArrayList;
import java.util.List;

import static com.dhirain.newsgo.utills.CategoryUtil.getCatogeryFromDisplay;
import static com.dhirain.newsgo.utills.CategoryUtil.getCatogeryToDisplay;

/**
 * Created by DJ on 14-09-2017.
 */

public class FilterListAdapter  extends RecyclerView.Adapter<FilterListViewHolder> {
    private static final String TAG = "ListAdapter";
    private List<KeyValueModel> list;
    private Context context;
    private boolean isPublisher;
    private CheckListner checkListner;
    private int lastPosition = -1;
    News currentNews;

    public FilterListAdapter(Context context) {
        this.context = context;
        this.checkListner = (CheckListner) context;
    }

    public void updateList(List<KeyValueModel> newRepo, boolean isPublisher) {
        list = new ArrayList<>();
        this.list = newRepo;
        this.isPublisher = isPublisher;
        this.lastPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public FilterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catagoery_row, parent, false);
        return new FilterListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterListViewHolder holder, int position) {
        if (list != null) {
            showCurrentItem(holder, list.get(position));

        }
        setAnimation(holder.checkBox,position);
    }

    private void showCurrentItem(final FilterListViewHolder holder, final KeyValueModel keyValueModel) {
        holder.checkBox.setText(isPublisher? keyValueModel.getKey(): getCatogeryToDisplay(keyValueModel.getKey()));
        holder.checkBox.setChecked(keyValueModel.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isPublisher){
                    checkListner.updatePublisherList(compoundButton.getText().toString(),b);
                }
                else {
                    checkListner.updateCatogeryList(getCatogeryFromDisplay(compoundButton.getText().toString()),b);
                }

            }
        });

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


    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else {
            return list.size();
        }
    }
}
