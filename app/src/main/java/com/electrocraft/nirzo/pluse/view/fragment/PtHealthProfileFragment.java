package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Key;

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

/**
 * This Fragment is for Insert and update Patient Health Info
 *
 * @author Faisal Mohammad
 * @since 2/25/2018
 */

public class PtHealthProfileFragment extends Fragment {

    private String mCodeBloodGroup;

    @BindView(R.id.edtWeight)
    EditText edtWeight;
    @BindView(R.id.edtBp)
    EditText edtBloodPressure;
    @BindView(R.id.edtFamilyDiseaseHistory)
    EditText edtFamilyDiseaseHistory;
    @BindView(R.id.edtExistingMedicalReport)
    EditText edtExistingMedicalReport;
    @BindView(R.id.spBloodGroup)
    Spinner spBloodGroup;
    private ProgressDialog pDialog;
    private String mPatientId="";


    public PtHealthProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_health_profile, container, false);
        ButterKnife.bind(this, view);
        getBloodGroup();
        mPatientId = getArguments().getString(Key.KEY_PATIENT_ID, "");
        getPatientHealthInfo(mPatientId);
        return view;
    }


    @OnClick(R.id.btn_ptn_healthProfile)
    public void saveHealthProfile() {

        String weight = edtWeight.getText().toString();
        String bloodPressure = edtBloodPressure.getText().toString();
        String familyDiseaseHistory = edtFamilyDiseaseHistory.getText().toString();
        String existingMedicalReport = edtExistingMedicalReport.getText().toString();
        savePatientHealthInfo(mPatientId, weight, bloodPressure, existingMedicalReport,  familyDiseaseHistory,mCodeBloodGroup);

    }

    /**
     * get the blood Group Data from db
     */
    private void getBloodGroup() {
        String blood_group_tag = "blood_group_tag";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getbloodgroup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Timber.d(response);

                        String BGCode = "";
                        String BGName = "";


                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);
                            List<SpinnerHelper> bloodGroupList = new ArrayList<>();
                            if (!object.isNull("data")) {

                                JSONArray array = object.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    BGCode = jsonObject.getString("BGCode");
                                    BGName = jsonObject.getString("BGName");

                                    SpinnerHelper helper = new SpinnerHelper(i, BGCode, BGName);
                                    bloodGroupList.add(helper);

                                }
                                loadBloodGroup(bloodGroupList);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                closeDialog();                                                                          // hide the progress dialog
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, blood_group_tag);

    }


    /**
     * load spinner of Blood Group
     */
    private void loadBloodGroup(List<SpinnerHelper> list) {


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spBloodGroup.setAdapter(adapter);

        spBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strBloodGroup = ((SpinnerHelper) spBloodGroup.getSelectedItem()).getDatabaseValue();
                mCodeBloodGroup = ((SpinnerHelper) spBloodGroup.getSelectedItem()).getDatabaseId();

                /*if (strLoginORegAs.equals("Doctor"))
                    isDoctorLogin = true;
                else
                    isDoctorLogin = false;*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

    private void savePatientHealthInfo(final String patientId, final String weight, final String bloodPressure,
                                       final String existingMedicalReport, final String familyDiseaseHistory, final String bloodGroup) {
        String tag = "patient_health_info_save";


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_NOT_AVALIBLE + " ",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        String id = "";
                        String msg = "";
                        closeDialog();
                        try {
                            JSONObject jos = new JSONObject(response);

                            msg = jos.getString("msg");


                            AlertDialogManager.showSuccessDialog(getActivity(), msg);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("More", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());

                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


             /*   params.put("father_name", fatherName);
                params.put("mother_name", motherName);
                params.put("dob", patientDOB);
                params.put("age", patientAge);
                params.put("presentAddress", patientPresentAdd);
                params.put("pri_prid", patientId);*/
                // api param active but not integrated
                // params.put("image", image);


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void getPatientHealthInfo(final String patientId) {
        String tag = "get_patient_personal_info";

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.API_NOT_AVALIBLE + "/" + patientId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();

                        String PPPI_PTPersonalInfoCode = "";
                        String PRI_PTID = "";
                        String PPPI_FatherName = "";
                        String PPPI_MotherName = "";
                        String DOB = "";
                        String PPPI_Age = "";
                        String PPPI_PresentAddress = "";
                        String PPPI_Photo = "";
                        closeDialog();
                        try {
                            JSONObject jos = new JSONObject(response);

                           /* if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONArray daArray = jos.getJSONArray("data");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);
                                        PPPI_PTPersonalInfoCode = object.getString("PPPI_PTPersonalInfoCode");
                                        PRI_PTID = object.getString("PRI_PTID");
                                        PPPI_FatherName = object.getString("PPPI_FatherName");
                                        PPPI_MotherName = object.getString("PPPI_MotherName");
                                        DOB = object.getString("DOB");
                                        PPPI_Age = object.getString("PPPI_Age");
                                        PPPI_PresentAddress = object.getString("PPPI_PresentAddress");
                                        PPPI_Photo = object.getString("PPPI_Photo");

                                    }
                                }

                            }
*/

                       /*     mfatherName = PPPI_FatherName;
                            mMotherName = PPPI_MotherName;
                            mPresentAddress = PPPI_PresentAddress;
                            mPatientDateOfBirth = DOB;
                            mAge = PPPI_Age;*/
                            setPatientProfileInfo();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("PatientPersonal", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());

                closeDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void setPatientProfileInfo() {

     /*   setDataToView(mfatherName, edtPtFatherName);
        setDataToView(mMotherName, edtPtMotherName);
        setDataToView(mPresentAddress, edtPtPresentAddress);

        tvPatientDOB.setText(mPatientDateOfBirth);
        tvPtAge.setText(mAge);*/


    }
    /**
     * set data to the View
     * @param str data
     * @param view editText
     */
    private void setDataToView(String str, EditText view) {
        if (str.length() > 0)
            view.setText(str);
        else
            view.setText("");
    }

}
