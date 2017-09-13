package com.dhirain.newsgo.ui.homeSreen.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhirain.newsgo.R;

/**
 * Created by DJ on 14-09-2017.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView titleText,url,catogery,publisher,host,timestamp;
    public ImageView like;
    public LinearLayout meta;
    public RelativeLayout parent;
    public CardView cardView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(R.id.projectName);
        url = (TextView) itemView.findViewById(R.id.url_text);
        catogery = (TextView) itemView.findViewById(R.id.catogery_text);
        publisher = (TextView) itemView.findViewById(R.id.publisher_text);
        host = (TextView) itemView.findViewById(R.id.hostname_text);
        timestamp = (TextView) itemView.findViewById(R.id.days_text);
        like = (ImageView) itemView.findViewById(R.id.like);
        parent = (RelativeLayout) itemView.findViewById(R.id.parentRR);
        meta = (LinearLayout) itemView.findViewById(R.id.meta_detail);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
    }
}
