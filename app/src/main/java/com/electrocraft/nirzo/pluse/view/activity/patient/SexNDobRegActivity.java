package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.electrocraft.nirzo.pluse.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SexNDobRegActivity extends AppCompatActivity {

    @BindView(R.id.btn_pt_reg_submit)
    protected Button ptn_reg_submit;                                                                //submit all patient data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_ndob_reg);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pt_reg_submit)
    public void submit(View view){
        startActivity(new Intent(SexNDobRegActivity.this,Home.class));
        finish();
    }

}
