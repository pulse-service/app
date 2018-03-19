package com.electrocraft.nirzo.pluse.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
public class LoginActivity extends AppCompatActivity {

    private boolean isDoctorLogin = false;
    private final int CAMERA_PERMISSION_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private ProgressDialog pDialog;
    private static final String TAG = "LoginActivity";

 /*   @BindView(R.id.rl_sub_patient)
    public RelativeLayout rl_sub;

    @BindView(R.id.rly_login)
    public RelativeLayout loginLayout;

    @BindView(R.id.ll_sub)
    public LinearLayout subLoginNRegLayout;*/

    private Context mContext = null;

    @BindView(R.id.edtPhone)
    public EditText edtPhone;

    @BindView(R.id.edtPassword)
    public EditText edtPassword;

    //@BindView(R.id.ivLogo)
    ImageView ivLogo;

    //@BindView(R.id.spLoginOrRegAs)
    //Spinner spLoginOrRegAs;

    private ConnectionDetector cd;
    private String strLoginORegAs;
    private String codeLoginORegAsCode;





/*    @BindView(R.id.rbGroupLogin)
    RadioGroup rbGroupLogin;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isDoctorLogin = getIntent().getExtras().getBoolean("isDoctor");
        ButterKnife.bind(this);

        ButterKnife.bind(this);

        if (mContext == null)
            mContext = this;

        // for internet check
        cd = new ConnectionDetector(this);





        if (!checkPermission()) {

            requestPermission();

        } else {

//            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_SHORT).show();

        }

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);



        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean audioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (audioAccepted && cameraAccepted) {

                    }

                    else {



                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{RECORD_AUDIO, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void onDoctorRegistrationButton() {
        startActivity(new Intent(mContext, DocRegistrationActivity.class));
    }

    public void onLoginClick(View view) {

        Log.d("wasi", "onLoginClick: ");
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


                                    startActivity(new Intent(LoginActivity.this, DoctorHomeActivity.class));

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
//               params.put("password", SharePref.getFireBaseToken(LoginActivity.this));

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




/*
package com.electrocraft.nirzo.pluse.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

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

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;


public class LoginActivity extends AppCompatActivity {

    private boolean isDoctorLogin = false;

    private final int CAMERA_PERMISSION_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;

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





*/
/*    @BindView(R.id.rbGroupLogin)
    RadioGroup rbGroupLogin;*//*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (mContext == null)
            mContext = this;

        // for internet check
        cd = new ConnectionDetector(this);



        loadLoginAsSpinner();

        if (!checkPermission()) {

            requestPermission();

        } else {

//            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_SHORT).show();

        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);



        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean audioAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (audioAccepted && cameraAccepted) {

                    }

                    else {



                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{RECORD_AUDIO, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

*/
/*    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Permission needed");
            builder.setTitle("App need the Camera Permission of your Device");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{CAMERA}, CAMERA_PERMISSION_CODE);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create();
            builder.show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }*//*


    @OnClick(R.id.btn_sub_login_doc)
    public void docLoginClick(View view) {
        viewVisibilityController();
        isDoctorLogin = true;
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












/**
 * @param phoneNo  phone number
 * @param password pass
 *//*

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
*/
