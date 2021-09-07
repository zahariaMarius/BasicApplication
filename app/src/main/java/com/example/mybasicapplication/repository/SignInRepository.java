package com.example.mybasicapplication.repository;

import androidx.lifecycle.LiveData;


import com.example.mybasicapplication.data.Resource;
import com.example.mybasicapplication.data.model.User;
import com.example.mybasicapplication.network.ApiClient;
import com.example.mybasicapplication.network.NetworkBoundResource;
import com.example.mybasicapplication.network.apiservice.SignInApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class SignInRepository {

    private final SignInApiService signInApiService;


    public SignInRepository() {
        this.signInApiService = ApiClient.getInstance().getSignInApiService();
    }

    public LiveData<Resource<User>> signIn() {
        return new NetworkBoundResource<User, User>() {

            @Override
            protected Call<User> createCall() {
                return signInApiService.signIn(new JsonObject());
            }

            @Override
            protected void saveCallResult(User data) {
            }

            @Override
            protected LiveData<User> loadFromDb() {
                return null;
            }

            @Override
            protected boolean shouldFetch(User data) {
                return false;
            }
        }.getAsLiveData();
    }

}
