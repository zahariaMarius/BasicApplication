package com.example.mybasicapplication.ui.signin;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mybasicapplication.data.Resource;
import com.example.mybasicapplication.data.model.User;
import com.example.mybasicapplication.repository.SignInRepository;


public class SignInViewModel extends ViewModel {

    private final SignInRepository signInRepository;

    public SignInViewModel(Context context) {
        this.signInRepository = new SignInRepository();
    }

    public LiveData<Resource<User>> signIn() {
        return this.signInRepository.signIn();
    }

}
