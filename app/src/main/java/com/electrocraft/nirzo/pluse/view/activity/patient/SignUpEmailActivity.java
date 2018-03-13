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
import com.electrocraft.nirzo.pluse.controller.util.SharePref;
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

            gotoOtpPage(patientName, email, phoneNo, password, valueCountryCode);


        }


    }

    private void gotoOtpPage(String patientName, String email, String phoneNo, String password, String valueCountryCode) {

        Intent patientOTP = new Intent(SignUpEmailActivity.this, PatientOtpActivity.class);

        patientOTP.putExtra(Key.KEY_PHONE_NO, phoneNo);
        patientOTP.putExtra(Key.KEY_COUNTRY_CODE, valueCountryCode);
        patientOTP.putExtra(Key.KEY_PATIENT_NAME, patientName);
        patientOTP.putExtra(Key.KEY_EMAIL, email);
        patientOTP.putExtra(Key.KEY_PASSWORD, password);

        startActivity(patientOTP);
        //            finish();
    }


}
