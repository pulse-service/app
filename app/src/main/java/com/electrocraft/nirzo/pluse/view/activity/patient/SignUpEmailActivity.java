package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.electrocraft.nirzo.pluse.R;

public class SignUpEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);
    }

    public void onSignContinueClick(View view) {
        //  todo: set the validation
        startActivity(new Intent(SignUpEmailActivity.this, OTP_Activity.class));
        finish();
    }
}
