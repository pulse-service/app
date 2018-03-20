package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by nirzo on 2/28/2018.
 */

public class DocOTPFragments extends Fragment implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {


    @BindView(R.id.btn_changeNo)
    Button btnChangeNo;

    @BindView(R.id.btn_resendOTP)
    Button btnResendOTP;

    @BindView(R.id.btn_otp_verify)
    Button btnOtpVerify;

    @BindView(R.id.login_pin_first_edittext)
    EditText mPinFirstDigitEdt;

    @BindView(R.id.login_pin_forth_edittext)
    EditText mPinForthDigitEditText;

    @BindView(R.id.login_pin_second_edittext)
    EditText mPinSecondDigitEditText;

    @BindView(R.id.login_pin_third_edittext)
    EditText mPinThirdDigitEditText;


    @BindView(R.id.login_pin_hidden_edittext)
    EditText mPinHiddenEditText;
    private ProgressDialog pDialog;

    //    private String mPhone = "";
    private String mDoctorName = "";
    private String mEmail = "";
    private String mPassword = "";
    private String mCountryCode = "";


    public DocOTPFragments() {
    }


    private String editTextToString(EditText editText) {
        return editText.getText().toString();
    }

    @OnClick(R.id.btn_otp_verify)
    public void onOTPVerifyClick(View view) {
        if (!AppConfig.SMS_OTP_OFF) {
            String inputOTP = editTextToString(mPinFirstDigitEdt) +
                    editTextToString(mPinSecondDigitEditText) +
                    editTextToString(mPinThirdDigitEditText) +
                    editTextToString(mPinForthDigitEditText);

//            Timber.e("Otp :" + inputOTP);
            if (otpCode.equals(inputOTP)) {
                registerADoctor(mDoctorName, mEmail, mPhoneNo, mPassword, mCountryCode);

            } else {
                AlertDialogManager.showErrorDialog(getActivity(), "Wrong OTP");
            }
        } else {
            registerADoctor(mDoctorName, mEmail, mPhoneNo, mPassword, mCountryCode);
      /*      Fragment frag = new DocProfileFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.docFrame, frag);
            ft.commit();*/
        }

    }

    @OnClick(R.id.btn_resendOTP)
    public void onResendOTPClick(View view) {
        if (mPhoneNo.length() > 0)
            generateFourDigitOTP(mPhoneNo);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    String mPhoneNo = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_otp, container, false);
        ButterKnife.bind(this, view);
//        ButterKnife.apply(btnOtpVerify, BKViewController.DISABLE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPhoneNo = bundle.getString(Key.KEY_PHONE_NO);
            mCountryCode = bundle.getString(Key.KEY_COUNTRY_CODE);
            mDoctorName = bundle.getString(Key.KEY_DOCTOR_NAME);
            mEmail = bundle.getString(Key.KEY_EMAIL);
            mPassword = bundle.getString(Key.KEY_PASSWORD);

        }

        if (!AppConfig.SMS_OTP_OFF)
            if (mPhoneNo != null && mPhoneNo.length() > 0)
                generateFourDigitOTP(mCountryCode + mPhoneNo);

        setPINListeners();
        return view;
    }

    String otpCode;

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

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);
        mPinFirstDigitEdt.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);
        mPinFirstDigitEdt.setOnKeyListener(this);
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
        setDefaultPinBackground(mPinFirstDigitEdt);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(this.mPinFirstDigitEdt);
            this.mPinFirstDigitEdt.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(this.mPinSecondDigitEditText);
            mPinFirstDigitEdt.setText(s.charAt(0) + "");
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

    /**
     * this is volley method to post verified Doctor's info for registration
     * if process is successful than user will Go home Page
     *
     * @param name        patient Name
     * @param email       patient  email
     * @param phoneNo     patient phone number
     * @param password    password
     * @param countryCode country Code
     */
    private void registerADoctor(final String name, final String email, final String phoneNo,
                                 final String password, final String countryCode) {
        String tag = "doc_log_in_tag";


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "doctorregistration",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("BORODIM", response);

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
                                    AppSharePreference.saveDoctorID(getActivity(), id);

                                    Fragment frag = new DocProfileFragment();
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.docFrame, frag);
                                    ft.commit();

//                                    startActivity(new Intent(PatientOtpActivity.this, PatientHomeActivity.class));
                                }
                            } else {
                                msg = jos.getString("msg");
                                AlertDialogManager.showErrorDialog(getActivity(), msg);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MOR", "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dname", name);
                params.put("demail", email);
                params.put("dpassword", password);
                params.put("dcountryCode", countryCode);
                params.put("dcontactNumber", phoneNo);
                params.put("dspecializat", "SP001");

            /*    :Ahsan
                :habib.cse51@gmail.com
                :123456
                :0001
                :018400403734
                :SP001
*/
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag);

    }

    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }
}
