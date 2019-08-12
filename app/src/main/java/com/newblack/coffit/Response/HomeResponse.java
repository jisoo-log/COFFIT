package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.Schedule;

import java.util.List;

public class HomeResponse extends PT {
    @SerializedName("ptComment")
    private PTcomment ptComment;
    @SerializedName("schedules")
    private List<Schedule> schedules;

    public PTcomment getPtComment() {
        return ptComment;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
