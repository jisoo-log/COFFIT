package com.newblack.coffit;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface APIInterface {

    @GET("trainers")
    Call<List<Trainer>> getTrainerList();

    @GET("PTs/students/{id}")
    Call

    @GET("schedules/trainer/{id}")
    Call<List<Date>> getAvailableList();
}
