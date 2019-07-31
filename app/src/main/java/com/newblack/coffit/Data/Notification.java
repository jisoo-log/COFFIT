package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//노티쪽은 나중에 백이랑 협의하고 다시 하자
public class Notification implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("contents")
    private String contents;
    @SerializedName("rejectMessage")
    private String rejectMsg;
    @SerializedName("schedule_id")
    private int scheduleId;
    @SerializedName("trainerId")
    private int trainerId;
    @SerializedName("studentId")
    private int studentId;


}
