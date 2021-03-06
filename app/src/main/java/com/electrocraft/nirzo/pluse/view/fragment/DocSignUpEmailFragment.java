package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null)
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_sign_up_email, container, false);
        ButterKnife.bind(this, view);
        loadCountryCodes();

        // hide the soft input
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

    @BindView(R.id.i_agree)
    AppCompatCheckBox checkBox;
    @OnClick(R.id.btnSignUpContinue)
    public void onSignContinueClick(View view) {
        Context context = getActivity();
        String phoneNo = edtDoctorPhone.getText().toString();
        String doctorName = edtDoctorName.getText().toString();
        String email = edtDoctorEmail.getText().toString();
        String password = edtDoctorPassword.getText().toString();

        if (doctorName.length() == 0) {

            AlertDialogManager.showMissingDialog(getActivity(), "Name Missing");
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
        else if (!checkBox.isChecked()) {
            Toast.makeText(context, "You need to agree to the terms and condition", Toast.LENGTH_SHORT).show();
        }
        else {


            gotoDoctorOTPPage(doctorName, email, phoneNo, password, valueCountryCode);


//                          Send data (mPhoneNo no) fragment to fragment

                        /*String phoneNo=valueCountryCode+edtDoctorPhone.getText().toString();
                        Bundle bundle = new Bundle();
                        bundle.putString(Key.KEY_PHONE_NO,phoneNo);

                        Fragment frag = new DocOTPFragments();
                        frag.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.docFrame, frag);
                        ft.commit();*/


//            Timber.d("Go to next  Fragment");
        }

    }

    private void gotoDoctorOTPPage(String name, String email, String phoneNo, String password, String valueCountryCode) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.KEY_PHONE_NO, phoneNo);
        bundle.putString(Key.KEY_COUNTRY_CODE, valueCountryCode);
        bundle.putString(Key.KEY_DOCTOR_NAME, name);
        bundle.putString(Key.KEY_EMAIL, email);
        bundle.putString(Key.KEY_PASSWORD, password);


        Fragment frag = new DocOTPFragments();
        frag.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.docFrame, frag);
        ft.commit();
    }

    @OnClick(R.id.docFbSign)
    public void regFBLogin() {
    }
    @OnClick(R.id.docLogin)
    public void regLogin(View view) {
        getActivity().onBackPressed();
    }
}
