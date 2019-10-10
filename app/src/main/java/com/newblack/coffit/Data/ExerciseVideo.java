package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ExerciseVideo {
    @SerializedName("id")
    int id;
    @SerializedName("videoFormat")
    String format;
    @SerializedName("date")
    Date date;
    @SerializedName("url")
    String url;
    @SerializedName("thumbnail_url")
    String thumbnail_url;
    @SerializedName("time_tag")
    String time_tag;
    @SerializedName("student_id")
    int student_id;
    @SerializedName("PT_id")
    int PT_id;
    @SerializedName("mission_id")
    int mission_id;

    public int getId() {
        return id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getTime_tag() {
        return time_tag;
    }

    public void setTime_tag(String time_tag) {
        this.time_tag = time_tag;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getPT_id() {
        return PT_id;
    }

    public int getMission_id() {
        return mission_id;
    }

}
