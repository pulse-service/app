package com.electrocraft.nirzo.pluse.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

/**
 * Created by nirzo on 2/28/2018.
 */

public class DocOTPFragments extends Fragment {

    public static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setEnabled(false);
        }
    };

    public static final ButterKnife.Action<View> ENABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setEnabled(true);
        }
    };

    @BindView(R.id.edtOtpInput)
    EditText edtOtpInput;

    @BindView(R.id.btn_changeNo)
    Button btnChangeNo;

    @BindView(R.id.btn_resendOTP)
    Button btnResendOTP;

    @BindView(R.id.btn_otp_verify)
    Button btnOtpVerify;



    public DocOTPFragments() {
    }


    @OnTextChanged(R.id.edtOtpInput)
    public void textChanged(CharSequence charSequence) {
        if (charSequence.length() > 3)
            ButterKnife.apply(btnOtpVerify,ENABLE);
        Timber.d("the otp" + charSequence);
    }


    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view){
        startActivity(new Intent(getActivity(),DoctorHomeActivity.class));
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_otp, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.apply(btnOtpVerify, DISABLE);
        return view;
    }


}
