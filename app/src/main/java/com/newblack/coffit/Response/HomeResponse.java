package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.PTComment;
import com.newblack.coffit.Data.Schedule;

import java.util.List;

public class HomeResponse {
    @SerializedName("pt")
    private PT pt;

    @SerializedName("ptComment")
    private PTComment ptComment;
    @SerializedName("schedules")
    private List<Schedule> schedules;

    public PT getPt(){return pt;}

    public PTComment getPtComment() {
        return ptComment;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
