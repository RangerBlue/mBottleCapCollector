package com.km.mbottlecapcollector.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PictureWrapper implements Parcelable {
    @SerializedName("id")
    private long id;
    @SerializedName("url")
    private String url;

    public PictureWrapper(long id, String url) {
        this.id = id;
        this.url = url;
    }

    protected PictureWrapper(Parcel in) {
        id = in.readLong();
        url = in.readString();
    }

    public static final Creator<PictureWrapper> CREATOR = new Creator<PictureWrapper>() {
        @Override
        public PictureWrapper createFromParcel(Parcel in) {
            return new PictureWrapper(in);
        }

        @Override
        public PictureWrapper[] newArray(int size) {
            return new PictureWrapper[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl(int pixels) {
        return url + "=w" + pixels;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(url);
    }
}
