package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.SharePref;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientOtpActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.SignUpEmailActivity;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * This class is to update  patient profile
 *
 * @author Faisal Mohammad
 * @since 2/25/2018.
 */

public class PtProfileFragment extends Fragment {

    private String mPatient;
    @BindView(R.id.btn_PtPersonalInfoSave)
    Button btn_PtPersonalInfoSave;


    @BindView(R.id.edtPtFatherName)
    EditText edtPtFatherName;

    @BindView(R.id.edtPtMotherName)
    EditText edtPtMotherName;

  /*  @BindView(R.id.edtPtDeathOfBirth)
    EditText edtPtDeathOfBirth;*/

    @BindView(R.id.edtPtPresentAddress)
    EditText edtPtPresentAddress;


    @BindView(R.id.tvPtAge)
    TextView tvPtAge;
    private ProgressDialog pDialog;


    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();
    @BindView(R.id.tvPatientDOB)
    public TextView reDOB;

    /**
     * DatePicker code Start
     **/
    public void updateDate() {
        reDOB.setText(format.format(calendar.getTime()));
    }

    public void setDate() {
        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };


    @OnClick(R.id.tvPatientDOB)
    public void patientDobClick() {
        setDate();
    }

    public PtProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_profile, container, false);
        ButterKnife.bind(this, view);
        mPatient = getArguments().getString(Key.KEY_PATIENT_ID, "");
        return view;
    }

    @OnClick(R.id.btn_PtPersonalInfoSave)
    public void onProfileSave(View view) {

    }

    private void savePatientPersonalInfo(final String patientId, final String fatherName, final String motherName,
                                         final String patientDOB, final String patientAge, final String patientPresentAdd) {
        String tag = "patient_personal_info_save";

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "patientprofilepersonalinfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        String id = "";
                        String msg = "";
                        closeDialog();
                        try {
                            JSONObject jos = new JSONObject(response);

                          /*  if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONObject object = jos.getJSONObject("data");
                                    id = object.getString("id");

//                                    Intent patientOTP = new Intent(SignUpEmailActivity.this, PatientOtpActivity.class);
//                                    patientOTP.putExtra(Key.KEY_PATIENT_ID, id);
                            *//*
                             save session id
                             *//*
//                                    SharePref.savePatientID(mContext, id);
//                                    patientOTP.putExtra(Key.KEY_PHONE_NO, valueCountryCode + phoneNo);
//
//                                    startActivity(patientOTP);
                                }
                            } else {
                                msg = jos.getString("msg");
                                AlertDialogManager.showErrorDialog(mContext, msg);

                            }*/

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


                params.put("father_name", fatherName);
                params.put("mother_name", motherName);
                params.put("dob", patientDOB);
                params.put("age", patientAge);
                params.put("presentAddress", patientPresentAdd);
                params.put("pri_prid", patientId);
                // api param active but not integrated 
                // params.put("image", image);


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

}
