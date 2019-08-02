package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable {
    @SerializedName("id")
    private int id;

    //state
    //  0 : 요청
    //  1 : 확정
    //  2 : 확정 뒤 변경 요청중
    //  3 : 거절 -> 삭제
    //  4 : 끝난 schedule
    @SerializedName("state")
    private int state;

    @SerializedName("date")
    private Date date;
    @SerializedName("start_time")
    private Date startTime;
    @SerializedName("end_time")
    private Date endTime;

    @SerializedName("memo")
    private String memo;
    @SerializedName("PT_id")
    private int PTId;
    @SerializedName("trainerId")
    private int trainerId;
    @SerializedName("studentId")
    private int studentId;

    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public Date getDate() {
        return date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getMemo() {
        return memo;
    }

    public int getPTId() {
        return PTId;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public int getStudentId() {
        return studentId;
    }



}
