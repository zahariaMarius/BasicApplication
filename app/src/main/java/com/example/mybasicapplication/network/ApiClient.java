package com.example.mybasicapplication.network;


import com.example.mybasicapplication.network.apiservice.SignInApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient apiClient;
    private SignInApiService signInApiService;

    private ApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("baseUrl")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.signInApiService = retrofit.create(SignInApiService.class);
    }

    public static ApiClient getInstance() {
        return apiClient == null ? new ApiClient() : apiClient;
    }

    public SignInApiService getSignInApiService() {
        return signInApiService;
    }
}
