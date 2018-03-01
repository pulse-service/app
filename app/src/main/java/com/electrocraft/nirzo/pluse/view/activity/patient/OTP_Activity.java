package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.electrocraft.nirzo.pluse.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OTP_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_otp);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view){
        startActivity(new Intent(OTP_Activity.this,SexNDobRegActivity.class));
    }
}
