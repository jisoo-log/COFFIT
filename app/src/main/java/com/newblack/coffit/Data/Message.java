package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Message {
    @SerializedName("id")
    private int id;
    @SerializedName("chatting_room_id")
    private int room_id;
    @SerializedName("message")
    private String message;
    @SerializedName("opponent")
    private String username;

    private String time;

    @SerializedName("createdAt")
    private Date date;
    @SerializedName("opponent_picture")
    private String pic;
    @SerializedName("type")
    private String type;
    @SerializedName("trainer_id")
    private int trainer_id;
    @SerializedName("student_id")
    private int student_id;


    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getPic(){
        return pic;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public static class Builder {

        private String mUsername;
        private String mMessage;
        private String mTime;
        private String mPic;
        private String mType;
        private Date mDate;


        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder pic(String pic){
            mPic= pic;
            return this;
        }

        public Builder time(Date d) {
            mDate = d;
            return this;
        }

        public Builder type(String type){
            mType = type;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.username = mUsername;
            message.message = mMessage;
            message.date = mDate;
            message.pic = mPic;
            message.type = mType;

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            message.time = format.format(mDate);
            return message;
        }
    }


}
