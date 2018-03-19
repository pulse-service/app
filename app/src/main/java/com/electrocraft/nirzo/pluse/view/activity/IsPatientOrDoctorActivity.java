package com.electrocraft.nirzo.pluse.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.util.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class IsPatientOrDoctorActivity extends AppCompatActivity {

    @BindView(R.id.btn_sign_as_patient)
    Button btnSignAsPatient;

    @BindView(R.id.btn_sign_as_doctor)
    Button btnSignAsDoctor;

    @Optional
    @OnClick({R.id.btn_sign_as_doctor, R.id.btn_sign_as_patient})
    public void buttonClickEvent(View view) {
        boolean isDoctorLogin = false;

        switch (view.getId()) {
            case R.id.btn_sign_as_doctor:
                isDoctorLogin = true;
                break;
            case R.id.btn_sign_as_patient:
                isDoctorLogin = false;
                break;
        }

        Intent intent = new Intent(IsPatientOrDoctorActivity.this, LoginActivity.class);
        intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, isDoctorLogin);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_doctor_patient);
        ButterKnife.bind(this);
    }
}
