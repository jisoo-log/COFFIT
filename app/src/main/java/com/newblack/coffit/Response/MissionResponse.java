package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.ExerciseVideo;
import com.newblack.coffit.Data.Mission;

public class MissionResponse extends Mission{
    @SerializedName("exerciseVideo")
    private ExerciseVideo video;

    public ExerciseVideo getVideo() {
        return video;
    }
}
