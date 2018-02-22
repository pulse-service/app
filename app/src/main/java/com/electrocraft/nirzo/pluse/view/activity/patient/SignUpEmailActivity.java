package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.notification.DialogFragments;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpEmailActivity extends AppCompatActivity  {


    @BindView(R.id.edt_email)
    EditText edt_pt_email;
    @BindView(R.id.edit_name)
    EditText edt_pt_name;
    @BindView(R.id.edt_phoneNo)
    EditText edt_pt_phone;
    @BindView(R.id.edt_password)
    EditText edt_pt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);
        ButterKnife.bind(this);

    }

    public void onSignContinueClick(View view) {

        if (edt_pt_name.getText().toString().length() == 0){

            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Name Missing");
        }

        else if (edt_pt_email.getText().toString().length() == 0)
            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Email Missing");
        else if (edt_pt_phone.getText().toString().length() == 0)
            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Phone Missing");
        else if (edt_pt_password.getText().toString().length() == 0)
            AlertDialogManager.showErrorDialog(SignUpEmailActivity.this, "Insert Password");
        else {
            startActivity(new Intent(SignUpEmailActivity.this, OTP_Activity.class));
            finish();
        }

    }
}
