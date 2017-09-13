package com.dhirain.newsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DJ on 14-09-2017.
 */

public class KeyValueModel implements Parcelable {
    private String key;
    private boolean isChecked = false;

    public KeyValueModel(String key, boolean isChecked) {
        this.key = key;
        this.isChecked = isChecked;
    }

    public KeyValueModel(String key) {
        this.key = key;
        this.isChecked = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    protected KeyValueModel(Parcel in) {
        key = in.readString();
        isChecked = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeByte((byte) (isChecked ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<KeyValueModel> CREATOR = new Parcelable.Creator<KeyValueModel>() {
        @Override
        public KeyValueModel createFromParcel(Parcel in) {
            return new KeyValueModel(in);
        }

        @Override
        public KeyValueModel[] newArray(int size) {
            return new KeyValueModel[size];
        }
    };

    @Override
    public String toString() {
        return "KeyValueModel{" +
                "key='" + key + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}