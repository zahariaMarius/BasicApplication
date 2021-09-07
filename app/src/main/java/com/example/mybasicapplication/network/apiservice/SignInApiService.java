package com.example.mybasicapplication.network.apiservice;

import com.example.mybasicapplication.data.model.User;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInApiService {

    @POST("auth")
    Call<User> signIn(@Body JsonObject body);

}
