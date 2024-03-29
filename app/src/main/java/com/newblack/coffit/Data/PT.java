package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import retrofit2.http.FormUrlEncoded;

public class PT implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("price")
    private int price;
    @SerializedName("total_number")
    private int totalNum;
    @SerializedName("rest_number")
    private int restNum;
    @SerializedName("start_date")
    private Date startDate;
    @SerializedName("end_date")
    private Date endDate;
    @SerializedName("trainer_id")
    private int trainerId;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("pt_room")
    private String ptRoom;



    public int getId(){
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public int getRestNum() {
        return restNum;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getPtRoom() {
        return ptRoom;
    }
}
