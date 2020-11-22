package com.km.mbottlecapcollector.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ValidateCapResponse {
    @SerializedName("duplicate")
    boolean isDuplicate;

    @SerializedName("similarCapsIDs")
    ArrayList<Long> similarCapsIDs;

    @SerializedName("similarCapsURLs")
    ArrayList<String> similarCapsURLss;

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean duplicate) {
        isDuplicate = duplicate;
    }

    public ArrayList<Long> getSimilarCapsIDs() {
        return similarCapsIDs;
    }

    public void setSimilarCapsIDs(ArrayList<Long> similarCapsIDs) {
        this.similarCapsIDs = similarCapsIDs;

    }

    public ArrayList<String> getSimilarCapsURLss() {
        return similarCapsURLss;
    }
}