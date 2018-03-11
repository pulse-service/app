package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class OTP_Activity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

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
    private String otpCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_otp);
        ButterKnife.bind(this);
        setPINListeners();
    }

    private void generateFourDigitOTP(final String phoneNo) {
        Random random = new Random();
        otpCode = String.format("%04d", random.nextInt(10000));

        getToken(phoneNo, otpCode);
    }

    private void getToken(final String phoneNo, final String otp) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_LINK + "auth/login",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("MOR", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String mToken = jsonObject.getString("token");

                            if (mToken.length() > 20)
                                sendOTP(phoneNo, otp, mToken);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());

                Toast.makeText(getActivity(), "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "user@user.com");
                params.put("password", "123456");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "hello");
    }

    private void sendOTP(final String phoneNo, final String otp, final String token) {
        String tag = "send_otp_tag";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.API_LINK
                + "sendsms/" + phoneNo + "/" + otp + "?token=" + token
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, tag);

    }

    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view) {
        startActivity(new Intent(OTP_Activity.this, PatientHomeActivity.class));
    }

    private void setDefaultPinBackground(EditText editText) {
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

    public static void setFocus(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }
    }

    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.general_border_box));
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

}
