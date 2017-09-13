package com.dhirain.newsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dhirain.newsgo.database.manager.DBManager;
import com.dhirain.newsgo.utills.DateUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DJ on 14-09-2017.
 */

public class News implements Parcelable {

    private Long _id;//for cupboard

    @SerializedName("ID")
    @Expose
    private int s_no;

    @SerializedName("TITLE")
    @Expose
    private String title;

    @SerializedName("URL")
    @Expose
    private String url;

    @SerializedName("PUBLISHER")
    @Expose
    private String publisher;

    @SerializedName("CATEGORY")
    @Expose
    private String catogery;

    @SerializedName("HOSTNAME")
    @Expose
    private String hostame;

    @SerializedName("TIMESTAMP")
    @Expose
    private long timestamp;

    private boolean isFavorite = false;

    public News() {}

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Long get_id() {
        return _id;
    }

    public int getS_no() {
        return s_no;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCatogery() {
        return catogery;
    }

    public String getHostame() {
        return hostame;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getDateInTimeStamp(){
        return DateUtil.getUTCString(timestamp);
    }

    public void setFavorite(boolean favorite) {
        try {
            isFavorite = favorite;
            DBManager.instance().updateFavToItem(s_no, favorite);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public String toString() {
        return "News{" +
                "_id=" + _id +
                ", s_no=" + s_no +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", publisher='" + publisher + '\'' +
                ", catogery='" + catogery + '\'' +
                ", hostame='" + hostame + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }



    protected News(Parcel in) {
        _id = in.readByte() == 0x00 ? null : in.readLong();
        s_no = in.readInt();
        title = in.readString();
        url = in.readString();
        publisher = in.readString();
        catogery = in.readString();
        hostame = in.readString();
        timestamp = in.readLong();
        isFavorite = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(_id);
        }
        dest.writeInt(s_no);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(publisher);
        dest.writeString(catogery);
        dest.writeString(hostame);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}