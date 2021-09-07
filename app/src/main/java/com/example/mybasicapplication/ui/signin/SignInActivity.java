package com.example.mybasicapplication.ui.signin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mybasicapplication.R;


public class SignInActivity extends AppCompatActivity {

    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        //signInViewModel = new ViewModelProvider(this, new SignInViewModelProvider(getApplicationContext())).get(SignInViewModel.class);
    }

    public void signIn() {
        this.signInViewModel.signIn().observe(this, userResource -> {
            if (userResource != null) {
                switch (userResource.status) {
                    case SUCCESS:
                        navToMainActivity();
                        break;
                    case ERROR:
                        break;
                    case LOADING:
                        break;
                }
            }
        });
    }

    private void navToMainActivity() {
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }
}