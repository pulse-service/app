package com.electrocraft.nirzo.pluse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.patient.Home;
import com.electrocraft.nirzo.pluse.view.patient.SignUpEmailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void onLoginClick(View view) {
        startActivity(new Intent(LoginActivity.this, Home.class));
    }
    public void onRegistrationClick(View view){
        startActivity(new Intent(LoginActivity.this, SignUpEmailActivity.class));
    }
}
