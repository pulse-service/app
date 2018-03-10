package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

/**
 * Created by nirzo on 2/28/2018.
 */

public class DocSignUpEmailFragment extends Fragment {

    private static final String TAG = "DocSignUpEmailFragment";

    @BindView(R.id.edt_email)
    EditText edtDoctorEmail;

    @BindView(R.id.edit_name)
    EditText edtDoctorName;

    @BindView(R.id.edt_phoneNo)
    EditText edtDoctorPhone;

    @BindView(R.id.edt_password)
    EditText edtDoctorPassword;

    @BindView(R.id.spCountryCode)
    Spinner spCountryCode;
    private ProgressDialog pDialog;
    private String strCountry;
    private String valueCountryCode;

    public DocSignUpEmailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_sign_up_email, container, false);
        ButterKnife.bind(this, view);
        loadCountryCodes();
        return view;
    }

    private void loadCountryCodes() {
        List<SpinnerHelper> list = new ArrayList<>();
        try {
            String response = AssetUtils.getJsonAsString("country_code.json", getContext());

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


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getContext(),
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
        Context context = getActivity();
        String phoneNo=edtDoctorPhone.getText().toString();

        if (edtDoctorName.getText().toString().length() == 0) {

            AlertDialogManager.showMissingDialog(getActivity(), "Name Missing");
        } else if (edtDoctorEmail.getText().toString().length() == 0)
            AlertDialogManager.showMissingDialog(context, "Email Missing");

        else if (!Util.isValidEmail(edtDoctorEmail.getText().toString()))
            AlertDialogManager.showMissingDialog(context, "Invalid Email");

        else if (phoneNo.length() == 0)
            AlertDialogManager.showMissingDialog(context, "Phone Missing");

        else if (!Util.isValidPhoneNo(phoneNo) || phoneNo.length() != 10)
            AlertDialogManager.showMissingDialog(context, "Invalid Phone Number");

        else if (edtDoctorPassword.getText().toString().length() == 0)
            AlertDialogManager.showErrorDialog(context, "Insert Password");
        else {

            getToken();


//            Timber.d("Go to next  Fragment");
        }

    }


    private void getToken() {


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_LINK + "auth/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("MOR", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String mToken = jsonObject.getString("token");
                    if (mToken.length() > 20)
                        registerADoctor(mToken);
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


    private void registerADoctor(final String token) {
        String patient_login_tag = "patient_log_in_tag";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_LINK + "doctorRegistration" + "?token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("BORODIM", response);


                pDialog.hide();
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getBoolean("flag")) {
                        Fragment frag = new DocOTPFragments();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.docFrame, frag);
                        ft.commit();

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
                params.put("dname", edtDoctorName.getText().toString());
                params.put("demail", edtDoctorEmail.getText().toString());
                params.put("dpassword", edtDoctorPassword.getText().toString());
                params.put("dcountryCode", valueCountryCode);
                params.put("dcontactNumber", edtDoctorPhone.getText().toString());

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, patient_login_tag);

    }

}
