package com.dhirain.newsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DJ on 14-09-2017.
 */

public class FilterModel implements Parcelable {
    private boolean isDescending = false;
    private ArrayList<KeyValueModel> catogeryKeyValue;
    private ArrayList<KeyValueModel>  publisherKeyValue;

    public FilterModel(boolean isDescending, ArrayList<KeyValueModel> catogeryKeyValue, ArrayList<KeyValueModel> publisherKeyValue) {
        this.isDescending = isDescending;
        this.catogeryKeyValue = catogeryKeyValue;
        this.publisherKeyValue = publisherKeyValue;
    }

    public boolean isDescending() {
        return isDescending;
    }

    public ArrayList<KeyValueModel> getCatogeryKeyValue() {
        return catogeryKeyValue;
    }

    public ArrayList<KeyValueModel> getPublisherKeyValue() {
        return publisherKeyValue;
    }

    public void setDescending(boolean descending) {
        isDescending = descending;
    }


    protected FilterModel(Parcel in) {
        isDescending = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            catogeryKeyValue = new ArrayList<KeyValueModel>();
            in.readList(catogeryKeyValue, KeyValueModel.class.getClassLoader());
        } else {
            catogeryKeyValue = null;
        }
        if (in.readByte() == 0x01) {
            publisherKeyValue = new ArrayList<KeyValueModel>();
            in.readList(publisherKeyValue, KeyValueModel.class.getClassLoader());
        } else {
            publisherKeyValue = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isDescending ? 0x01 : 0x00));
        if (catogeryKeyValue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(catogeryKeyValue);
        }
        if (publisherKeyValue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(publisherKeyValue);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FilterModel> CREATOR = new Parcelable.Creator<FilterModel>() {
        @Override
        public FilterModel createFromParcel(Parcel in) {
            return new FilterModel(in);
        }

        @Override
        public FilterModel[] newArray(int size) {
            return new FilterModel[size];
        }
    };

    @Override
    public String toString() {
        return "FilterModel{" +
                "isDescending=" + isDescending +
                ", catogeryKeyValue=" + catogeryKeyValue.toString() +
                ", publisherKeyValue=" + publisherKeyValue.toString() +
                '}';
    }
}
