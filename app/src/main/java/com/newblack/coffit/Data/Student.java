package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Student implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("age")
    private int age;
    @SerializedName("gender")
    private String gender;
    @SerializedName("phone_number")
    private String phoneNum;
    @SerializedName("fcm_token")
    private String fcmToken;
    @SerializedName("picture")
    private String picture;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
