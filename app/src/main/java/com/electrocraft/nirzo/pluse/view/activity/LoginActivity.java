package com.electrocraft.nirzo.pluse.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.network.ConnectionDetector;
import com.electrocraft.nirzo.pluse.controller.util.SharePref;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DocRegistrationActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.SignUpEmailActivity;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Key;
import com.electrocraft.nirzo.pluse.view.viewhelper.BKViewController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    private boolean isDoctorLogin = false;

    private ProgressDialog pDialog;
    private static final String TAG = "LoginActivity";

    @BindView(R.id.rl_sub_patient)
    public RelativeLayout rl_sub;

    @BindView(R.id.rly_login)
    public RelativeLayout loginLayout;

    @BindView(R.id.ll_sub)
    public LinearLayout subLoginNRegLayout;

    private Context mContext = null;

    @BindView(R.id.edtPhone)
    public EditText edtPhone;

    @BindView(R.id.edtPassword)
    public EditText edtPassword;

    @BindView(R.id.ivLogo)
    ImageView ivLogo;

    @BindView(R.id.spLoginOrRegAs)
    Spinner spLoginOrRegAs;

    private ConnectionDetector cd;
    private String strLoginORegAs;
    private String codeLoginORegAsCode;





/*    @BindView(R.id.rbGroupLogin)
    RadioGroup rbGroupLogin;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (mContext == null)
            mContext = this;


        cd = new ConnectionDetector(this);

        loadLoginAsSpinner();


    }

    @OnClick(R.id.btn_sub_login_doc)
    public void docLoginClick(View view) {
        viewVisibilityController();
        isDoctorLogin = true;
    }

    /* @OnCheckedChanged({R.id.rbPatient, R.id.rbDoctor})
     public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
         if (checked) {
             switch (button.getId()) {
                 case R.id.rbPatient:
                     // do stuff
                     isDoctorLogin = false;
                     break;
                 case R.id.rbDoctor:
                     // do stuff
                     isDoctorLogin = true;
                     break;
             }
         }
     }
 */
    @OnClick(R.id.btn_sub_reg_doc)
    public void onDoctorRegistrationButton() {
        startActivity(new Intent(mContext, DocRegistrationActivity.class));
    }

    public void onLoginClick(View view) {

        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();


        if (cd.isConnectingToInternet()) {

            if (phone.length() != 0 && password.length() != 0) {
                if (!isDoctorLogin) {
                    loginPatient(phone, password);
                } else {

                    loginDoctor(phone, password);
                }


            } else {
                AlertDialogManager.showErrorDialog(mContext, "Insert phone Number & password");
            }

        } else {
            AlertDialogManager.showErrorDialog(mContext, "No internet Connection");
        }

    }

    public void onRegistrationClick(View view) {
        if (!isDoctorLogin) {
            startActivity(new Intent(LoginActivity.this, SignUpEmailActivity.class));
        } else
            onDoctorRegistrationButton();
    }

    @OnClick(R.id.labelLLayout)
    public void onTextViewClick() {
        if (subLoginNRegLayout.getVisibility() == View.GONE)
            subLoginNRegLayout.setVisibility(View.VISIBLE);
        else
            subLoginNRegLayout.setVisibility(View.GONE);

        if (rl_sub.getVisibility() == View.VISIBLE) {
            viewGone(loginLayout);
            viewGone(ivLogo);

        }

    }


    private void loadLoginAsSpinner() {

        String[] msg = {"Patient", "Doctor", "Login / Registration As"};
        List<SpinnerHelper> list = new ArrayList<>();
        for (int i = 0; i < msg.length; i++) {
            SpinnerHelper helper = new SpinnerHelper(i, "0" + String.valueOf(i), msg[i]);
            list.add(helper);
        }


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(mContext, R.layout.rsc_spinner_text, list) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint((String) getItem(getCount()).getDatabaseValue()); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;      // you don't display last item. It is used as hint.
            }
        };

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spLoginOrRegAs.setAdapter(adapter);
        spLoginOrRegAs.setSelection(adapter.getCount());                                            //display hint
        spLoginOrRegAs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strLoginORegAs = ((SpinnerHelper) spLoginOrRegAs.getSelectedItem()).getDatabaseValue();
                codeLoginORegAsCode = ((SpinnerHelper) spLoginOrRegAs.getSelectedItem()).getDatabaseId();

                if (strLoginORegAs.equals("Doctor"))
                    isDoctorLogin = true;
                else
                    isDoctorLogin = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void viewGone(View view) {
        ButterKnife.apply(view, BKViewController.GONE);
    }

    private void viewVisible(View view) {
        ButterKnife.apply(view, BKViewController.VISIBLE);
    }


    public void onSubLoginClick(View view) {
        viewVisibilityController();

        isDoctorLogin = false;
    }


    private void viewVisibilityController() {
        if (loginLayout.getVisibility() == View.GONE) {

            viewVisible(ivLogo);
            viewVisible(loginLayout);

            viewGone(subLoginNRegLayout);
        } else
            loginLayout.setVisibility(View.GONE);
    }


    private void loginPatient(final String phoneNo, final String password) {
        String patient_login_tag = "patient_log_in_tag";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "patientregistration_login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("MAL", response);


                        String id = "";

                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("status").equals("success")) {
                                if (!object.isNull("data")) {
                                    JSONObject obj = object.getJSONObject("data");
                                    id = obj.getString("id");
                                    SharePref.savePatientID(mContext, id);
                                    Intent intent = new Intent(LoginActivity.this, PatientHomeActivity.class);
                                    intent.putExtra(Key.KEY_PATIENT_ID, id);
                                    startActivity(intent);

                                }
                            } else
                                AlertDialogManager.showErrorDialog(LoginActivity.this, "Invalid User or password");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("phone_number", phoneNo);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, patient_login_tag);

    }


    /**
     * {
     "status": "success",
     "data": {
     "id": "DR000000001"
     },
     "msg": "login successful"
     }
     * @param phoneNo sdf
     * @param password dsfd
     */
    private void loginDoctor(final String phoneNo, final String password) {
        String patient_login_tag = "patient_log_in_tag";

        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
        }
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "doctorlogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("MAL", response);


                        String id = "";

                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("status").equals("success")) {
                                if (!object.isNull("data")) {
                                    JSONObject obj = object.getJSONObject("data");


                                    id = obj.getString("id");
                                    SharePref.saveDoctorID(mContext, id);
                                    Intent intent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
//                                    intent.putExtra(Key.KEY_PATIENT_ID, id);
                                    startActivity(intent);

                                }
                            } else
                                AlertDialogManager.showErrorDialog(LoginActivity.this, "Invalid User or password");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("phone_number", phoneNo);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

}
