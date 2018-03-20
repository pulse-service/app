package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class PatientOtpActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    @BindView(R.id.login_pin_first_edittext)
    EditText mPinFirstDigitEditText;

/*    @BindView(R.id.login_pin_forth_edittext)
    EditText mPinForthDigitEditText;

    @BindView(R.id.login_pin_second_edittext)
    EditText mPinSecondDigitEditText;

    @BindView(R.id.login_pin_third_edittext)
    EditText mPinThirdDigitEditText;*/

    /*    EditText mPinSixthDigitEditText;*/
    @BindView(R.id.login_pin_hidden_edittext)
    EditText mPinHiddenEditText;
    private String otpCode;
    private String mPhone = "";
    private String mPatientName = "";
    private String mEmail = "";
    private String mPasssword = "";
    private String mCountryCode = "";

    private Context mContext;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_otp);
        ButterKnife.bind(this);
        setPINListeners();

        Intent intent = getIntent();
        mPhone = intent.getStringExtra(Key.KEY_PHONE_NO);


        mPatientName = intent.getStringExtra(Key.KEY_PATIENT_NAME);
        mEmail = intent.getStringExtra(Key.KEY_EMAIL);
        mPasssword = intent.getStringExtra(Key.KEY_PASSWORD);
        mCountryCode = intent.getStringExtra(Key.KEY_COUNTRY_CODE);
//        String string = intent.getStringExtra(Key.KEY_PHONE_NO);


        mContext = this;
        if (!AppConfig.SMS_OTP_OFF)
            if (mPhone.length() > 0)
                generateFourDigitOTP(mCountryCode + mPhone);
    }

    private String editTextToString(EditText editText) {
        return editText.getText().toString();
    }

    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view) {
        if (!AppConfig.SMS_OTP_OFF) {
            String inputOTP = editTextToString(mPinFirstDigitEditText);

//            Timber.d("Otp :" + inputOTP);
            if (otpCode.equals(inputOTP)) {


                registerPatient(mPatientName, mEmail, mPhone, mPasssword, mCountryCode);

            } else {
                AlertDialogManager.showErrorDialog(mContext, "Wrong OTP");
            }
        } else
            startActivity(new Intent(PatientOtpActivity.this, PatientHomeActivity.class));


    }

    private void generateFourDigitOTP(final String phoneNo) {
        Random random = new Random();
        otpCode = String.format("%04d", random.nextInt(10000));

        sendOTP(phoneNo, otpCode);
    }


    private void sendOTP(final String phoneNo, final String otp) {
        String tag = "send_otp_tag";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK
                + "sendsms/" + phoneNo + "/" + otp,
                new Response.Listener<String>() {
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
        //mPinHiddenEditText.addTextChangedListener(this);
        //mPinFirstDigitEditText.setOnFocusChangeListener(this);
/*        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);*/
        //mPinFirstDigitEditText.setOnKeyListener(this);
/*        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);*/
        //mPinHiddenEditText.setOnKeyListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
/*        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);*/

       /* if (s.length() == 0) {
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
        }*/
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

    /**
     * this is volley method to post verified patient's info for registration
     * if process is successful than user will Go home Page
     *
     * @param name        patient Name
     * @param email       patient  email
     * @param phoneNo     patient phone number
     * @param password    password
     * @param countryCode country Code
     */
    private void registerPatient(final String name, final String email, final String phoneNo,
                                 final String password, final String countryCode) {


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "patientregistration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                String id = "";
                String msg = "";
                closeDialog();
                try {
                    JSONObject jos = new JSONObject(response);

                    if (jos.getString("status").equals("success")) {
                        if (!jos.isNull("data")) {
                            JSONObject object = jos.getJSONObject("data");
                            id = object.getString("id");

                           /*  save patient id*/
                            AppSharePreference.savePatientID(mContext, id);
                            Intent intent=new Intent(PatientOtpActivity.this, PatientHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        msg = jos.getString("msg");
                        AlertDialogManager.showErrorDialog(mContext, msg);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("More", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                //
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PRI_name", name);
                params.put("PRI_password", password);
                params.put("PRI_countryCode", countryCode);
                params.put("PRI_phone", phoneNo);
                params.put("PRI_email", email);


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "token");
    }

    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

    public void otpChangeNumber(View view) {
    }

    public void otpResendOTP(View view) {
    }
}

/*public class PatientOtpActivity extends AppCompatActivity *//*implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher*//* {

*//*    @BindView(R.id.login_pin_first_edittext)
    EditText mPinFirstDigitEditText;*//*

 *//*   @BindView(R.id.login_pin_forth_edittext)
    EditText mPinForthDigitEditText;*//*

*//*    @BindView(R.id.login_pin_second_edittext)
    EditText mPinSecondDigitEditText;*//*

*//*    @BindView(R.id.login_pin_third_edittext)
    EditText mPinThirdDigitEditText;*//*

    *//*    EditText mPinSixthDigitEditText;*//*
   *//* @BindView(R.id.login_pin_hidden_edittext)


    EditText mPinHiddenEditText;*//*


    @BindView(R.id.login_otp_edittext)
    EditText login_otp_edittext;

    private String otpCode;
    private String mPhone = "";
    private String mPatientName = "";
    private String mEmail = "";
    private String mPasssword = "";
    private String mCountryCode = "";

    private Context mContext;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_otp);
        ButterKnife.bind(this);
        setPINListeners();

        Intent intent = getIntent();
        mPhone = intent.getStringExtra(Key.KEY_PHONE_NO);


        mPatientName = intent.getStringExtra(Key.KEY_PATIENT_NAME);
        mEmail = intent.getStringExtra(Key.KEY_EMAIL);
        mPasssword = intent.getStringExtra(Key.KEY_PASSWORD);
        mCountryCode = intent.getStringExtra(Key.KEY_COUNTRY_CODE);
//        String string = intent.getStringExtra(Key.KEY_PHONE_NO);


        mContext = this;
        if (!AppConfig.SMS_OTP_OFF)
            if (mPhone.length() > 0)
                generateFourDigitOTP(mCountryCode + mPhone);
    }

    private String editTextToString(EditText editText) {
        return editText.getText().toString();
    }

    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view) {
        // for development mode
        if (!AppConfig.SMS_OTP_OFF) {

            String inputOTP = editTextToString(login_otp_edittext);

//            Timber.d("Otp :" + inputOTP);
            if (otpCode.equals(inputOTP)) {


                registerPatient(mPatientName, mEmail, mPhone, mPasssword, mCountryCode);

            } else {
                AlertDialogManager.showErrorDialog(mContext, "Wrong OTP");
            }
        } else
            startActivity(new Intent(PatientOtpActivity.this, PatientHomeActivity.class));


    }

    private void generateFourDigitOTP(final String phoneNo) {
        Random random = new Random();
        otpCode = String.format("%04d", random.nextInt(10000));

        sendOTP(phoneNo, otpCode);
    }


    private void sendOTP(final String phoneNo, final String otp) {
        String tag = "send_otp_tag";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK
                + "sendsms/" + phoneNo + "/" + otp,
                new Response.Listener<String>() {
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


   *//* private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.general_border_box));
    }*//*

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
*//*        mPinHiddenEditText.addTextChangedListener(this);
        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);
        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);*//*
    }

  *//*  @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }*//*

    *//*@Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       *//**//* setDefaultPinBackground(mPinFirstDigitEditText);
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
        }*//**//*
    }*//*


    public void hideSoftKeyboard(EditText editText) {
        if (editText != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
*//*
    @Override
    public void afterTextChanged(Editable s) {

    }*//*

*//*    @Override
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
    }*//*

    public void showSoftKeyboard(EditText editText) {
        if (editText != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        }
    }

*//*    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }*//*

    *//**
 * this is volley method to post verified patient's info for registration
 * if process is successful than user will Go home Page
 *
 * @param name        patient Name
 * @param email       patient  email
 * @param phoneNo     patient phone number
 * @param password    password
 * @param countryCode country Code
 * <p>
 * hide the progress dialog
 *//*
    private void registerPatient(final String name, final String email, final String phoneNo,
                                 final String password, final String countryCode) {


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "patientregistration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                String id = "";
                String msg = "";
                closeDialog();
                try {
                    JSONObject jos = new JSONObject(response);

                    if (jos.getString("status").equals("success")) {
                        if (!jos.isNull("data")) {
                            JSONObject object = jos.getJSONObject("data");
                            id = object.getString("id");

                           *//*  save patient id*//*
                            AppSharePreference.savePatientID(mContext, id);

                            startActivity(new Intent(PatientOtpActivity.this, PatientHomeActivity.class));
                        }
                    } else {
                        msg = jos.getString("msg");
                        AlertDialogManager.showErrorDialog(mContext, msg);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("More", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                //
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PRI_name", name);
                params.put("PRI_password", password);
                params.put("PRI_countryCode", countryCode);
                params.put("PRI_phone", phoneNo);
                params.put("PRI_email", email);


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "token");
    }

    *//**
 * hide the progress dialog
 *//*
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

    public void otpChangeNumber(View view) {
    }

    public void otpResendOTP(View view) {
    }

}*/
