package com.electrocraft.nirzo.pluse.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.R;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, IsPatientOrDoctorActivity.class));

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
