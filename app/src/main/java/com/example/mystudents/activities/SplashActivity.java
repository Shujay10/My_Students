package com.example.mystudents.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mystudents.MainActivity;
import com.example.mystudents.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private static int splashTimer = 2000;
    //private static int splashTimer = 10000;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mStore;

    FirebaseDatabase database;

    Intent toLogin;
    Intent toMain;


    @Override
    protected void onStart() {
        super.onStart();

        splashHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        toLogin = new Intent(SplashActivity.this, LogInActivity.class);
        toMain = new Intent(SplashActivity.this, MainActivity.class);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mStore = FirebaseFirestore.getInstance();

        splashHandler();

    }

    private void splashHandler(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(toLogin);

                if(mUser == null)
                    startActivity(toLogin);
                else
                    startActivity(toMain);

            }
        },splashTimer);

    }

}