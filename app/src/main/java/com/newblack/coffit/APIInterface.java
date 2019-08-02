package com.newblack.coffit;

import com.newblack.coffit.Data.PT;
import com.newblack.coffit.Data.Trainer;
import com.newblack.coffit.Response.HomeResponse;
import com.newblack.coffit.Response.TrainerResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("home")
    Call<TrainerResponse> getMain();

    @GET("trainers")
    Call<List<Trainer>> getTrainerList();

    @GET("trainers/{id}")
    Call<Trainer> getTrainer(@Path("id") int trainerId);

    @FormUrlEncoded
    @POST("PTs")
    Call<PT> postPT(@FieldMap HashMap<String,Object> param);

    @GET("PTs/students/{id}")
    Call<List<HomeResponse>> getPT(@Path("id") int studentId);

    @GET("schedules/trainer/{id}")
    Call<List<Date>> getAvailableList();
}
