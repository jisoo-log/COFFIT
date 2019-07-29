package com.newblack.coffit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class Trainer implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("summary")
    private String summary;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private int price;
    @SerializedName("career")
    private String career;
    @SerializedName("picture_url")
    private String pictureURL; //picture_url
    @SerializedName("phone_number")
    private String phoneNum; //phone_number
    @SerializedName("fcm_token")
    private String fcmToken; //fcm_token
    @SerializedName("total_star")
    private int star; //total_star
    @SerializedName("num_review")
    private int numReview; //num_review
    @SerializedName("createdAt")
    public Date createdAt;

    public Trainer(int id, String username, String summary, int star, int numReview){
        //나중엔 없애야 함
        this.id = id;
        this.username = username;
        this.summary = summary;
        this.star = star;
        this.numReview = numReview;
    }

    //token은 어떻게 해야할지 고민
    public Trainer(int id, String username, String summary, int star, int numReview, int price, String pictureURL, String phoneNum){
        this.id = id;
        this.username = username;
        this.summary = summary;
        this.star = star;
        this.numReview = numReview;
        this.price = price;
        this.pictureURL = pictureURL;
        this.phoneNum = phoneNum;
    }


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getCareer() {
        return career;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public int getStar() {
        return star;
    }

    public int getNumReview() {
        return numReview;
    }

    public Date getAt(){
        return createdAt;
    }


}
