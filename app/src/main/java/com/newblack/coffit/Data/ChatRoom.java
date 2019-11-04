package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class ChatRoom {
    @SerializedName("id")
    private int id;
    @SerializedName("last_message")
    private String message;
    @SerializedName("trainer_name")
    private String trainer_name;
    @SerializedName("created_at")
    private Date date;

    private String time;
    @SerializedName("trainer_picture")
    private String trainer_pic;
    @SerializedName("trainer_id")
    private int trainer_id;
    @SerializedName("student_id")
    private int student_id;

    @SerializedName("student_name")
    private String student_name;
    @SerializedName("student_picture")
    private String student_pic;

    public String getStudent_name() {
        return student_name;
    }

    public String getStudent_pic() {
        return student_pic;
    }


    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(this.date);
    }

    public int getTrainer_id() {
        return trainer_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    private int not_seen;
    private Message last_msg;

    public Message getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(Message last_msg) {
        this.last_msg = last_msg;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public String getTrainer_pic(){
        return trainer_pic;
    }


    public int getNot_seen(){
        return not_seen;
    }

    public static class Builder {

        private int mId;
        private String mTitle;
        private String mMessage;
        private String mTime;
        private String mPic;
        private int mMsg;

        public ChatRoom.Builder id(int id) {
            mId = id;
            return this;
        }

        public ChatRoom.Builder title(String title) {
            mTitle = title;
            return this;
        }

        public ChatRoom.Builder message(String message) {
            mMessage = message;
            return this;
        }

        public ChatRoom.Builder pic(String pic){
            mPic= pic;
            return this;
        }

        public ChatRoom.Builder time(String time) {
            mTime = time;
            return this;
        }

        public ChatRoom.Builder newMsg(int msg){
            mMsg = msg;
            return this;
        }

        public ChatRoom build() {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.trainer_name = mTitle;
            chatRoom.message = mMessage;
            chatRoom.time = mTime;
            chatRoom.trainer_pic = mPic;
            chatRoom.not_seen = mMsg;
            return chatRoom;
        }
    }
}
