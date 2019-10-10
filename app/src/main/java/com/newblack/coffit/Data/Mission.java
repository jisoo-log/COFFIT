package com.newblack.coffit.Data;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Mission {
    @SerializedName("id")
    int id;
    @SerializedName("date")
    Date date;
    @SerializedName("rate")
    int rate;
    @SerializedName("has_video")
    boolean has_video;
    @SerializedName("is_converted")
    boolean converted;
    @SerializedName("comment")
    String comment;
    @SerializedName("contents")
    String content;
    @SerializedName("trainer_id")
    int trainer_id;
    @SerializedName("student_id")
    int student_id;
    @SerializedName("pt_id")
    int pt_id;
    @SerializedName("preSignedUrl")
    String videoUrl;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public boolean is_converted() {return converted; }

    public void setConverted(boolean converted) {
        this.converted = converted;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getTrainer_id() {
        return trainer_id;
    }


    public int getStudent_id() {
        return student_id;
    }


    public int getPt_id() {
        return pt_id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}

