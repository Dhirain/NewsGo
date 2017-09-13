package com.dhirain.newsgo.ui.filterScreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.dhirain.newsgo.R;

/**
 * Created by DJ on 14-09-2017.
 */

class FilterListViewHolder extends RecyclerView.ViewHolder {
    //public TextView textView;
    public CheckBox checkBox;

    public FilterListViewHolder(View itemView) {
        super(itemView);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
    }
}