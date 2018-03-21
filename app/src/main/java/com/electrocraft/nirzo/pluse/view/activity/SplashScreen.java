package com.electrocraft.nirzo.pluse.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;


public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 3000;
public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    AppSharePreference.getDoctorID(context);
                    Log.d("sss", "docid: "+AppSharePreference.getDoctorID(context));
                    Log.d("sss", "patid: "+AppSharePreference.getPatientID(context));

                    if(AppSharePreference.getDoctorID(context).equals("")&&AppSharePreference.getPatientID(context).equals("")){
                        startActivity(new Intent(SplashScreen.this, LoginAsActivity.class));
                    }else if(AppSharePreference.getDoctorID(context).equals("") && !AppSharePreference.getPatientID(context).equals("")){
                        startActivity(new Intent(SplashScreen.this, PatientHomeActivity.class));
                    }else if(AppSharePreference.getDoctorID(context).equals("") && !AppSharePreference.getPatientID(context).equals("")){
                        startActivity(new Intent(SplashScreen.this, DoctorHomeActivity.class));

                    }
                }catch (Exception e){
                    Log.d("sss", "error: ");
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}


/*package com.electrocraft.nirzo.pluse.view.activity;

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
}*/
