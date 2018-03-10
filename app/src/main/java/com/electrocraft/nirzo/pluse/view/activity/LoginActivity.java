package com.electrocraft.nirzo.pluse.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.network.ConnectionDetector;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DocRegistrationActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.SignUpEmailActivity;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    private boolean docLogin = false;

    private ProgressDialog pDialog;
    private static final String TAG = "LoginActivity";

    @BindView(R.id.rl_sub_patient)
    public RelativeLayout rl_sub;

    @BindView(R.id.rly_login)
    public RelativeLayout rly_login;

    @BindView(R.id.ll_sub)
    public LinearLayout sub;

    private Context mContext = null;

    @BindView(R.id.edtPhone)
    public EditText edtPhone;

    @BindView(R.id.edtPassword)
    public EditText edtPassword;


    private ConnectionDetector cd;

    @OnClick(R.id.btn_sub_login_doc)
    public void docLoginClick(View view) {
        viewVisibilityController();
        docLogin = true;
    }

    String mToken;

    @BindView(R.id.rbGroupLogin)
    RadioGroup rbGroupLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (mContext == null)
            mContext = this;


        cd = new ConnectionDetector(this);

        /*rbGroupLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);


                if (rb.getText().equals("doctor"))
                    docLogin = true;
                else
                    docLogin = false;
            }
        });*/
    }


    @OnCheckedChanged({R.id.rbPatient, R.id.rbDoctor})
    public void onRadioButtonCheckChanged(CompoundButton button, boolean checked) {
        if (checked) {
            switch (button.getId()) {
                case R.id.rbPatient:
                    // do stuff
                    docLogin = false;
                    break;
                case R.id.rbDoctor:
                    // do stuff
                    docLogin = true;
                    break;
            }
        }
    }

    @OnClick(R.id.btn_sub_reg_doc)
    public void onDoctorRegistrationButton() {
        startActivity(new Intent(mContext, DocRegistrationActivity.class));
    }

    public void onLoginClick(View view) {

        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();


        if (cd.isConnectingToInternet()) {

            if (phone.length() != 0 && password.length() != 0) {


                getToken();

            } else {
                AlertDialogManager.showErrorDialog(mContext, "Insert phone no & password");
            }

        } else {
            AlertDialogManager.showErrorDialog(mContext, "No internet Connection");
        }

    }

    public void onRegistrationClick(View view) {
        if (!docLogin) {
            startActivity(new Intent(LoginActivity.this, SignUpEmailActivity.class));
        } else
            onDoctorRegistrationButton();
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
        viewVisibilityController();

        docLogin = false;
    }


    private void viewVisibilityController() {
        if (rly_login.getVisibility() == View.GONE) {
            rly_login.setVisibility(View.VISIBLE);
            sub.setVisibility(View.GONE);
        } else
            rly_login.setVisibility(View.GONE);
    }


    private void loginPatient(final String phoneNo, final String token) {
        String patient_login_tag = "patient_log_in_tag";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.API_LINK + "checkpatientlogin/" + phoneNo + "?token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("MAL", response);

                String PRI_PTID = "";
                String PRI_PTName = "";
                String ACS_CountryCode = "";
                String PRI_Phone = "";
                String PRI_Email = "";

                pDialog.hide();
                try {
                    JSONObject object = new JSONObject(response);

                    if (!object.isNull("result")) {

                        JSONArray array = object.getJSONArray("result");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            PRI_PTID = jsonObject.getString("PRI_PTID");
                            PRI_PTName = jsonObject.getString("PRI_PTName");
                            ACS_CountryCode = jsonObject.getString("ACS_CountryCode");
                            PRI_Phone = jsonObject.getString("PRI_Phone");
                            PRI_Email = jsonObject.getString("PRI_Email");
                        }

                        if (PRI_PTName.length() > 0) {
                            if (!docLogin) {
                                Intent intent = new Intent(LoginActivity.this, PatientHomeActivity.class);
                                intent.putExtra("PTID", PRI_PTID);
                                intent.putExtra("PTName", PRI_PTName);
                                startActivity(intent);
                            } else
                                startActivity(new Intent(LoginActivity.this, DoctorHomeActivity.class));
                        } else {
                            AlertDialogManager.showErrorDialog(LoginActivity.this, "Invalid User");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void getToken() {


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_LINK + "auth/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("MOR", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    mToken = jsonObject.getString("token");
                    if (mToken.length() > 20)
                        loginPatient(edtPhone.getText().toString(), mToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                Toast.makeText(LoginActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
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

/*    private void heatStoke() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = AppConfig.API_LINK + "user_list";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("DAM", response);
                pDialog.hide();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly8xOTIuMTY4LjEuNy9lbGNfYXBpL3B1YmxpYy9hcGkvYXV0aC9sb2dpbiIsImlhdCI6MTUyMDE3MTA3NCwiZXhwIjoxNTIwMTc0Njc0LCJuYmYiOjE1MjAxNzEwNzQsImp0aSI6ImRsemlZRHoya29rckhKeXgifQ.nBicqrL3zYmxkCs0E2C-Y0IOaSdYunUqqZM4vWiVMP4");
                return params;
            }
        };


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }*/
}
