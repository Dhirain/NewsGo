package com.dhirain.newsgo.model.comparator;

import com.dhirain.newsgo.model.News;

import java.util.Comparator;

/**
 * Created by Dhirain Jain on 14-09-2017.
 */

public class DescendingDateComparator implements Comparator<News> {
    @Override
    public int compare(News repo1, News repo2) {
        if(repo1.getTimestamp()<repo2.getTimestamp())
            return 1;
        else
            return -1;
    }
}