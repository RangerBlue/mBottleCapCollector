package com.km.mbottlecapcollector.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Cap {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileLocation() {
        return fileLocation;
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
}
