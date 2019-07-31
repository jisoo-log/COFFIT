package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class TrainerSchedule implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("startTime")
    private Date startTime;
    @SerializedName("availability")
    private boolean availability;

    public int getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public boolean isAvailability() {
        return availability;
    }
}
