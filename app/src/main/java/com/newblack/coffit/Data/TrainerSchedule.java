package com.newblack.coffit.Data;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TrainerSchedule implements Serializable {
    @SerializedName("id")
    private int id;
//    @SerializedName("start_time")
//    private Date startTime;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("available")
    private boolean availability;

    private Date startTime_date;

    public int getId() {
        return id;
    }

//    public Date getStartTime() {
//        return startTime;
//    }
    public String getStartTime() {
        return startTime;
    }

    public boolean isAvailability() {
        return availability;
    }

    public String getStringTime(){
        Date date = DateUtils.stringToDate(startTime);
        int hour = date.getHours();
        int min = date.getMinutes();
        System.out.println("hour :" + hour + " min : "+min);
        return hour + ":" + min;
    }
}
