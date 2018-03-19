package com.electrocraft.nirzo.pluse.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.electrocraft.nirzo.pluse.R;

public class LoginAsActivity extends AppCompatActivity {

    private boolean isDoctorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as);
    }

    public void asDocClick(View view) {
        isDoctorLogin = true;
        startLogin();
    }

    public void asPatClick(View view) {
        isDoctorLogin = false;
        startLogin();
    }

    private void startLogin(){
        Intent mIntent = new Intent(this, LoginActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putBoolean("isDoctor", isDoctorLogin);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }
}
