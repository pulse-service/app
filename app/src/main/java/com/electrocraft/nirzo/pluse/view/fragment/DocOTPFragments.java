package com.electrocraft.nirzo.pluse.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.viewhelper.BKViewController;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by nirzo on 2/28/2018.
 */

public class DocOTPFragments extends Fragment implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {



  /*  @BindView(R.id.edtOtpInput)
    EditText edtOtpInput;*/

    @BindView(R.id.btn_changeNo)
    Button btnChangeNo;

    @BindView(R.id.btn_resendOTP)
    Button btnResendOTP;

    @BindView(R.id.btn_otp_verify)
    Button btnOtpVerify;

    @BindView(R.id.login_pin_first_edittext)
    EditText mPinFirstDigitEditText;

    @BindView(R.id.login_pin_forth_edittext)
    EditText mPinForthDigitEditText;

    @BindView(R.id.login_pin_second_edittext)
    EditText mPinSecondDigitEditText;

    @BindView(R.id.login_pin_third_edittext)
    EditText mPinThirdDigitEditText;

    /*    EditText mPinSixthDigitEditText;*/
    @BindView(R.id.login_pin_hidden_edittext)
    EditText mPinHiddenEditText;



    public DocOTPFragments() {
    }


 /*   @OnTextChanged(R.id.edtOtpInput)
    public void textChanged(CharSequence charSequence) {
        if (charSequence.length() > 3)
            ButterKnife.apply(btnOtpVerify,ENABLE);
        Timber.d("the otp" + charSequence);
    }*/


    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view){
//        startActivity(new Intent(getActivity(),DoctorHomeActivity.class));

        Fragment frag = new DocProfileFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.docFrame,frag);
        ft.commit();
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
//        ButterKnife.apply(btnOtpVerify, BKViewController.DISABLE);
        setPINListeners();
        return view;
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);
        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);
        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    public static void setFocus(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    public void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.general_border_box));
    }

    public void setViewBackground(View view, Drawable background) {
        if (view != null && background != null) {
            if (Build.VERSION.SDK_INT >= 17) {
                view.setBackground(background);
            } else {
                view.setBackgroundDrawable(background);
            }
        }
    }
    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.general_border_box));
    }




    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(this.mPinFirstDigitEditText);
            this.mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(this.mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 2) {
            setFocusedPinBackground(this.mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 3) {
            setFocusedPinBackground(this.mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 4) {
            setDefaultPinBackground(this.mPinForthDigitEditText);
            this.mPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(mPinForthDigitEditText);
        }
    }


    public void hideSoftKeyboard(EditText editText) {
        if (editText != null) {
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.login_pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                    return;
                }
                return;
            case R.id.login_pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                    return;
                }
                return;
            case R.id.login_pin_third_edittext:
                if (hasFocus) {
                    setFocus(this.mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                    return;
                }
                return;
            case R.id.login_pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                    return;
                }
                return;

            default:
                return;
        }
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText != null) {
            ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}
