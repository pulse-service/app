package com.electrocraft.nirzo.pluse.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DocRegistrationActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.Home;
import com.electrocraft.nirzo.pluse.view.activity.patient.SignUpEmailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.rl_sub_patient)
    public RelativeLayout rl_sub;
    @BindView(R.id.rly_login)
    public RelativeLayout rly_login;
    @BindView(R.id.ll_sub)
    public LinearLayout sub;

    private Context mContext=null;

  //  @BindView(R.id.btn_sub_login)
    //public Button btn_subLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (mContext == null)
            mContext=this;

    }

    @OnClick(R.id.btn_sub_reg_doc)
    public void onDoctorRegistrationButton(){
        startActivity(new Intent(mContext, DocRegistrationActivity.class));
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(LoginActivity.this, Home.class));
    }

    public void onRegistrationClick(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpEmailActivity.class));
    }

    public void onTextViewClick(View view) {
        if (sub.getVisibility() == View.GONE)
            sub.setVisibility(View.VISIBLE);
        else
            sub.setVisibility(View.GONE);

        if (rl_sub.getVisibility() == View.VISIBLE)
            rly_login.setVisibility(View.GONE);
    }

    public void onSubLoginClick(View view) {
        if (rly_login.getVisibility() == View.GONE) {
            rly_login.setVisibility(View.VISIBLE);
            sub.setVisibility(View.GONE);
        } else
            rly_login.setVisibility(View.GONE);
    }
}
