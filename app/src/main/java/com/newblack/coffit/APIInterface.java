package com.newblack.coffit;

import com.newblack.coffit.Data.Trainer;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("trainers")
    Call<List<Trainer>> getTrainerList();

//    @GET("PTs/students/{id}")
//    Call

    @GET("schedules/trainer/{id}")
    Call<List<Date>> getAvailableList();
}
