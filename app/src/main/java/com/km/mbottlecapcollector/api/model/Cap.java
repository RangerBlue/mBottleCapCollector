package com.km.mbottlecapcollector.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cap implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("fileLocation")
    private String fileLocation;

    @SerializedName("googleDriveID")
    private String googleDriveID;

    @SerializedName("capName")
    private String capName;

    @SerializedName("creationDate")
    private String creationDate;

    public Cap(int id, String fileLocation, String googleDriveID, String capName, String creationDate) {
        this.id = id;
        this.fileLocation = fileLocation;
        this.googleDriveID = googleDriveID;
        this.capName = capName;
        this.creationDate = creationDate;
    }

    protected Cap(Parcel in) {
        id = in.readInt();
        fileLocation = in.readString();
        googleDriveID = in.readString();
        capName = in.readString();
        creationDate = in.readString();
    }

    public static final Creator<Cap> CREATOR = new Creator<Cap>() {
        @Override
        public Cap createFromParcel(Parcel in) {
            return new Cap(in);
        }

        @Override
        public Cap[] newArray(int size) {
            return new Cap[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getFileLocation(int pixels) {
        if (pixels <= 0) {
            return fileLocation;
        }
        return fileLocation + "=w" + pixels;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getGoogleDriveID() {
        return googleDriveID;
    }

    public void setGoogleDriveID(String googleDriveID) {
        this.googleDriveID = googleDriveID;
    }

    public String getCapName() {
        return capName;
    }

    public void setCapName(String capName) {
        this.capName = capName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(fileLocation);
        parcel.writeString(googleDriveID);
        parcel.writeString(capName);
        parcel.writeString(creationDate);
    }
}
