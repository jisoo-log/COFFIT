package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.Schedule;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Data.TrainerSchedule;

import java.io.Serializable;
import java.util.List;

public class TrainerScheduleResponse implements Serializable {
    @SerializedName("trainer")
    private Trainer trainer;
    @SerializedName("availableTime")
    private List<TrainerSchedule> availables;
//    @SerializedName("schedules")
//    private List<Schedule> schedules;

    public Trainer getTrainer() {
        return trainer;
    }

    public List<TrainerSchedule> getAvailables() {
        return availables;
    }

//    public List<Schedule> getSchedules(){
//        return schedules;
//    }
}
