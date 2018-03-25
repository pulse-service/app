package com.electrocraft.nirzo.pluse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorPrescriptionActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private JitsiMeetView jitsiMeetView;

    private boolean isPatient;
    private String mAppointCode;
    private String mPatientId;
    private String mDoctorId;
    private String mCosultrationDate;
    private String mConStartTime;
    private String mConEndTime;
    private String mCi_AM_PM;

//    @Override
//    public void onBackPressed() {
//        if (!JitsiMeetView.onBackPressed()) {
//            // Invoke the default handler if it wasn't handled by React.
//            super.onBackPressed();
//
//
//        }
//        startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameJitsiMeet = findViewById(R.id.frameJitsiMeet);
        isPatient = getIntent().getBooleanExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);

        if (!isPatient) {
            mAppointCode = getIntent().getStringExtra("appointCode");
            mPatientId=getIntent().getStringExtra("patientId");
            mDoctorId=getIntent().getStringExtra("doctorId");
            mCosultrationDate=getIntent().getStringExtra("consultationDate");
            mCi_AM_PM=getIntent().getStringExtra("consultation_am_pm");
      /*      intent.putExtra("appointCode", "APPT-00000001");
            intent.putExtra("patientId", modelList.get(position).getPatientID());
            intent.putExtra("doctorId", modelList.get(position).getDoctorID());
            intent.putExtra("consultationDate", modelList.get(position).getAppointmentDate());
            intent.putExtra("consultation_am_pm", modelList.get(position).getInTime_AMOrPM());
                *//*
                    , mDoctorId
                    , "MT002", mCosultrationDate,
                    mConStartTime, mConEndTime,
                    mCi_AM_PM*/

            DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
            mConStartTime  = dfTime.format(Calendar.getInstance().getTime());

        }

        jitsiMeetView = new JitsiMeetView(this);

        frameJitsiMeet.addView(jitsiMeetView);

        jitsiMeetView.setListener(new JitsiMeetViewAdapter() {
            @Override
            public void onConferenceFailed(Map<String, Object> data) {
                super.onConferenceFailed(data);
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConferenceJoined(Map<String, Object> data) {
                super.onConferenceJoined(data);
//                Toast.makeText(MainActivity.this, "Joined", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConferenceLeft(Map<String, Object> data) {
                super.onConferenceLeft(data);

                Intent intent;
                if (isPatient) {
                    intent = new Intent(MainActivity.this, PatientHomeActivity.class);
                    startActivity(intent);
                } else {
                    DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
                    mConEndTime  = dfTime.format(Calendar.getInstance().getTime());

                    saveDoctorConsultation(mAppointCode, mPatientId, mDoctorId, "MT002", mCosultrationDate,
                            mConStartTime, mConEndTime,
                            mCi_AM_PM, "58");

                /*    intent = new Intent(MainActivity.this, DoctorPrescriptionActivity.class);
                    intent.putExtra("consult_id", "999");
                    startActivity(intent);*/
                }


            }

            @Override
            public void onConferenceWillJoin(Map<String, Object> data) {
                super.onConferenceWillJoin(data);
//                Toast.makeText(MainActivity.this, "Join", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConferenceWillLeave(Map<String, Object> data) {
                super.onConferenceWillLeave(data);
//                Toast.makeText(MainActivity.this, "Leave", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadConfigError(Map<String, Object> data) {
                super.onLoadConfigError(data);
            }
        });


        try {
            // jitsiMeetView.loadURL(new URL("https://180.148.210.139/Pulse"));
            jitsiMeetView.loadURL(new URL("https://meet.jit.si/Pulse"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        jitsiMeetView.dispose();
        jitsiMeetView = null;

        JitsiMeetView.onHostDestroy(this);


    }

    @Override
    public void onNewIntent(Intent intent) {
        JitsiMeetView.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        JitsiMeetView.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        JitsiMeetView.onHostPause(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * APPT_AppointmentCode:APPT-00000001
     * PRI_PTID:ECL-00000001
     * DRI_DrID:DR000000001
     * CMTI_MediaTypeCode:MT002
     * CI_ConsultationDate:2018-03-15
     * CI_ConsultationStartTime:02:03:58
     * CI_ConsultationEndTime:03:01:11
     * CI_AMOrPM:PM
     * CI_ConsultationPeriodInMinute:58
     * <p>
     * :APPT-00000001
     * :ECL-00000001
     * :DR000000001
     * :
     * :2018-03-15
     * :02:03:58
     * :03:01:11
     * :PM
     * :58
     */
    private void saveDoctorConsultation(final String appointmentCode, final String ptId, final String docId,
                                        final String mediaTypeCode, final String consultationDate, final String consultationStartTime,
                                        final String consultationEndTime, final String ci_AMOrPM, final String consultationPeriodInMinute) {
        String patient_login_tag = "doc_search_tag";
     /*   if (pDialog == null)
            pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");*/
        //pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "saveDoctorconsultationinfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("response", response);


//                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);

                            /*{
                                "status": "success",
                                    "data": {
                                "id": "CI-00000005"
                            },
                                "msg": "Parsonal Information Save successfully completed"
                            }*/

                            String id = "";
                            if (!object.isNull("data")) {

                                JSONObject data = object.getJSONObject("data");
                                if (!data.isNull("id")) {
                                    id = data.getString("id");

                                    Intent intent = new Intent(MainActivity.this, DoctorPrescriptionActivity.class);
                                    intent.putExtra("consult_id", id);
                                    startActivity(intent);
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("vooleyError", "Error: " + error.getMessage());
                // hide the progress dialog
//                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("APPT_AppointmentCode", appointmentCode);
                params.put("PRI_PTID", ptId);
                params.put("DRI_DrID", docId);
                params.put("CMTI_MediaTypeCode", mediaTypeCode);
                params.put("CI_ConsultationDate", consultationDate);
                params.put("CI_ConsultationStartTime", consultationStartTime);
                params.put("CI_ConsultationEndTime", consultationEndTime);
                params.put("CI_AMOrPM", ci_AMOrPM);
                params.put("CI_ConsultationPeriodInMinute", consultationPeriodInMinute);


                return params;
            }
        };

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }
}
