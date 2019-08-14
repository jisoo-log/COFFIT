package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Noti {

    @SerializedName("to_whom")
    private String toWhom;
    @SerializedName("contents")
    private String content;
    @SerializedName("request_date")
    private Date time;
    @SerializedName("type")
    private String type; //어떤 종류의 노티인지 저장할 필요 존재
    private boolean read;

    private String makerId;


    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return read;
    }
}
