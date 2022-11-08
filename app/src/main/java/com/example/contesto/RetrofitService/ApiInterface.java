package com.example.contesto.RetrofitService;

import com.example.contesto.Models.ContestObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("api/v1/all")
    Call<List<ContestObject>> getAllContestsFromApi();
}
