package com.newblack.coffit.Response;

import com.google.gson.annotations.SerializedName;
import com.newblack.coffit.Data.Banner;
import com.newblack.coffit.Data.Trainer;

import java.util.List;

public class HomeResponse {
    @SerializedName("banner")
    private List<Banner> banners;
    @SerializedName("trainer_list")
    private List<Trainer> trainers;

    public List<Banner> getBanners() {
        return banners;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }
}
