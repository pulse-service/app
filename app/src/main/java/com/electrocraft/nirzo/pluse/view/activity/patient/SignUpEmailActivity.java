package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.electrocraft.nirzo.pluse.view.util.Key;
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
    private String strCountry;
    private String valueCountryCode;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sign_up_email);
        ButterKnife.bind(this);
        loadCountryCodes();
        mContext = this;

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

        spCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCountry = ((SpinnerHelper) spCountryCode.getSelectedItem()).getDatabaseValue();
                valueCountryCode = ((SpinnerHelper) spCountryCode.getSelectedItem()).getDatabaseId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btnSignUpContinue)
    public void onSignContinueClick(View view) {
        Context context = SignUpEmailActivity.this;

        String phoneNo = edt_pt_phone.getText().toString();
        String patientName = edt_pt_name.getText().toString();
        String email = edt_pt_email.getText().toString();
        String password = edt_pt_password.getText().toString();

        if (patientName.length() == 0) {

            AlertDialogManager.showMissingDialog(context, "Name Missing");
        } else if (email.length() == 0)
            AlertDialogManager.showMissingDialog(context, "Email Missing");

        else if (!Util.isValidEmail(email))
            AlertDialogManager.showMissingDialog(context, "Invalid Email");

        else if (phoneNo.length() == 0)
            AlertDialogManager.showMissingDialog(context, "Phone Missing");

        else if (!Util.isValidPhoneNo(phoneNo) || phoneNo.length() != 10)
            AlertDialogManager.showMissingDialog(context, "Invalid Phone Number");

        else if (password.length() == 0)
            AlertDialogManager.showErrorDialog(context, "Insert Password");

        else if (password.length() < 6)
            AlertDialogManager.showErrorDialog(context, "Password must be 6 digit");
        else {

            registerPatient(patientName, email, phoneNo, password, valueCountryCode);

//            finish();
        }

    }

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

                            Intent patientOTP = new Intent(SignUpEmailActivity.this, PatientOtpActivity.class);
                            patientOTP.putExtra(Key.KEY_PATIENT_ID, id);
                            patientOTP.putExtra(Key.KEY_PHONE_NO, valueCountryCode+phoneNo);

                            startActivity(patientOTP);
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
}
