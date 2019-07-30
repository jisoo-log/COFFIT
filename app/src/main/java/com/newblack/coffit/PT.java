package com.newblack.coffit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class PT implements Serializable {

    @SerializedName("price")
    private int price;
    @SerializedName("total_number")
    private int totalNum;
    @SerializedName("rest_number")
    private int restNum;
    @SerializedName("start_date")
    private Date startDate;
    @SerializedName("trainerId")
    private int trainerId;
    @SerializedName("studentId")
    private int studentId;

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
}