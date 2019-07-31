package com.newblack.coffit;

import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Response.HomeResponse;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("home")
    Call<HomeResponse> getMain();

    @GET("trainers")
    Call<List<Trainer>> getTrainerList();

    @GET("trainers/{id}")
    Call<Trainer> getTrainer(@Path("id") int trainerId);

//    @GET("PTs/students/{id}")
//    Call

    @GET("schedules/trainer/{id}")
    Call<List<Date>> getAvailableList();
}
