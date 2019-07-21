package com.newblack.coffit;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Trainer implements Serializable {
    private int id;
    private String username;
    private String summary;
    private String description;
    private int price;
    private String career;
    private String pictureURL; //picture_url
    private String phoneNum; //phone_number
    private String fcmToken; //fcm_token
    private int star; //total_star
    private int numReview; //num_review

    public Trainer(int id, String username, String summary, int star, int numReview){
        //나중엔 없애야 함
        this.id = id;
        this.username = username;
        this.summary = summary;
        this.star = star;
        this.numReview = numReview;
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


}
