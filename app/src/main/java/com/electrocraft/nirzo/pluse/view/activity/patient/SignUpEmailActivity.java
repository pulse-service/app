package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AssetUtils;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SignUpEmailActivity extends AppCompatActivity {


    @BindView(R.id.edt_email)
    EditText edt_pt_email;
    @BindView(R.id.edit_name)
    EditText edt_pt_name;
    @BindView(R.id.edt_phoneNo)
    EditText edt_pt_phone;
    @BindView(R.id.edt_password)
    EditText edt_pt_password;


    @BindView(R.id.spCountryCode)
    Spinner spCountryCode;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sign_up_email);
        ButterKnife.bind(this);
        loadCountryCodes();

    }

    /**
     * load spinner of Doctor's Specialization
     */
    private void loadCountryCodes() {
        List<SpinnerHelper> list = new ArrayList<>();
        try {
            String response = AssetUtils.getJsonAsString("country_code.json", this);

            JSONObject object = new JSONObject(response);
            if (!object.isNull("result")) {
                JSONArray array = object.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = (JSONObject) array.get(i);
                    String shortName = object1.getString("shortName");
                    String accessCode = object1.getString("accessCode");

                    SpinnerHelper helper = new SpinnerHelper(i, accessCode, shortName + "(" + accessCode + ")");
                    list.add(helper);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(this,
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spCountryCode.setAdapter(adapter);
    }

    @OnClick(R.id.btnSignUpContinue)
    public void onSignContinueClick(View view) {

        String phoneNo = edt_pt_phone.getText().toString();
        String patientName = edt_pt_name.getText().toString();
        String email = edt_pt_email.getText().toString();
        String password = edt_pt_password.getText().toString();

        if (patientName.length() == 0) {

            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Name Missing");
        } else if (email.length() == 0)
            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Email Missing");

        else if (!Util.isValidEmail(email))
            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Invalid Email");

        else if (phoneNo.length() == 0)
            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Phone Missing");

        else if (!Util.isValidPhoneNo(phoneNo) || phoneNo.length() != 10)
            AlertDialogManager.showMissingDialog(SignUpEmailActivity.this, "Invalid Phone Number");

        else if (password.length() == 0)
            AlertDialogManager.showErrorDialog(SignUpEmailActivity.this, "Insert Password");

        else {

            registerPatient(patientName,email,phoneNo,password);

//            finish();
        }

    }

    private void registerPatient(final String name,final String email, final String phoneNo,final String password) {


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_LINK + "auth/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                AppController.getInstance().getRequestQueue().getCache().clear();
                try {
                    JSONObject jos = new JSONObject(response);
                    /*if (!jos.isNull("token")) {
                        token = jos.getString("token");
                        if (token.length() > 6)
                            heatStoke(token);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("More", response);
                pDialog.hide();

               startActivity(new Intent(SignUpEmailActivity.this, OTP_Activity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("phoneNo", phoneNo);
                params.put("password", password);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "token");
    }
}
