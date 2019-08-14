package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.Noti;
import com.newblack.coffit.Data.Schedule;

import java.io.Serializable;

public class NotiResponse extends Noti implements Serializable {
//    private Noti noti;
    @SerializedName("schedule")
    private Schedule schedule;

//    public Noti getNoti() {
//        return noti;
//    }

    public Schedule getSchedule() {
        return schedule;
    }
}
