package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Banner implements Serializable {
    @SerializedName("id")
    private int id;

//    썸네일용인데 일단은 사용 안함
//    @SerializedName("thumbnail_url")
//    private String url;

    @SerializedName("picture_url")
    private String pictureURL;

    public int getId() {
        return id;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}
