package com.newblack.coffit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface APIInterface {

    @GET("trainers")
    Call<List<Trainer>> getTrainerList();
}
