package com.electrocraft.nirzo.pluse.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DocRegistrationActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.SignUpEmailActivity;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Key;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
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
    private FirebaseAuth mAuth;
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

    private CallbackManager mCallbackManager;



/*    @BindView(R.id.rbGroupLogin)
    RadioGroup rbGroupLogin;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isDoctorLogin = getIntent().getExtras().getBoolean("isDoctor");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = findViewById(R.id.fblogin);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("wasiun", ": " + response.getRawResponse());
                                try {
                                    JSONObject jsonObject = new JSONObject(response.getRawResponse());
                                    Log.d("wasiun", ": " + jsonObject.optString("name"));
                                    Log.d("wasiun", ": " + jsonObject.optString("email"));
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("picture").getJSONObject("data");
                                    Log.d("wasiun", ": " + jsonObject1.optString("url"));
                                    Toast.makeText(mContext, "Welcome "+jsonObject.optString("name"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(mContext, "Sorry, you'll have to login using your mobile number for now.", Toast.LENGTH_LONG).show();

                                    //showDialog(LoginActivity.this, jsonObject.optString("name") + "\n " + jsonObject.optString("email" + " \n" + jsonObject.optString("id")), jsonObject1.optString("url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(mContext, "Sorry, couldn't login", Toast.LENGTH_SHORT).show();
                // ...
            }
        });

// ...


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
   /* private void handleFacebookAccessToken(AccessToken token) {


        Log.d("token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }*/


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

                    } else {


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

    @OnClick(R.id.btn_login)
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

    @OnClick(R.id.btn_signup)
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
                                    AppSharePreference.savePatientID(mContext, id);
                                    Intent intent = new Intent(LoginActivity.this, PatientHomeActivity.class);
                                    intent.putExtra(Key.KEY_PATIENT_ID, id);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                params.put("device_token", AppSharePreference.getFireBaseToken(LoginActivity.this));
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
                                    AppSharePreference.saveDoctorID(mContext, id);
                                    Intent intent = new Intent(LoginActivity.this, DoctorHomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                params.put("device_token", AppSharePreference.getFireBaseToken(LoginActivity.this));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            updateUI(currentUser);
        }

    }

    private void updateUI(FirebaseUser currentUser) {


        Intent intent = new Intent(getApplicationContext(), PatientHomeActivity.class);
        startActivity(intent);
        finish();
    }



}



