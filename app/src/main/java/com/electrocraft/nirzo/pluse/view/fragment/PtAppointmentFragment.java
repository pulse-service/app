package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.SharePref;
import com.electrocraft.nirzo.pluse.model.AppointmentModel;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;
import com.electrocraft.nirzo.pluse.view.MainActivity;
import com.electrocraft.nirzo.pluse.view.adapter.DoctorSearchListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.PatientsAppointmentListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.RecyclerTouchListener;
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

/**
 * @author Faisal
 * @since 3/17/2018
 */

public class PtAppointmentFragment extends Fragment {

    @BindView(R.id.recyAppointment)
    RecyclerView rcvAppointment;

    List<AppointmentModel> modelList = new ArrayList<>();

    /* @BindView(R.id.lr_btn_Call)
     Button lr_btn_Call;*/
    private String mPatientId;
    private String mPatientAppointDate;
    private ProgressDialog pDialog;
    private String TAG = "PtAppointmentFragment";

/*    @OnClick(R.id.lr_btn_Call)
    public void dimChole() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }*/

    private PatientsAppointmentListAdapter mAdapter;

    public PtAppointmentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_s_appointment_list, container, false);
        ButterKnife.bind(this, view);
       /* APPT_AppointmentDate*/
        setUpAdapter();
        mPatientId = SharePref.getPatientID(getActivity());


        Log.d("Sala", " mPatientId:" + mPatientId  );
        getPatientAppointment(mPatientId);
        return view;
    }

    private void setUpAdapter() {
        mAdapter = new PatientsAppointmentListAdapter(modelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rcvAppointment.setLayoutManager(mLayoutManager);
        rcvAppointment.setItemAnimator(new DefaultItemAnimator());
        rcvAppointment.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rcvAppointment.setAdapter(mAdapter);
        rcvAppointment.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rcvAppointment, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {



                startActivity(new Intent(getActivity(), MainActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

 /*   http://180.148.210.139:8081/pulse_api/api/saveCallRequest
    receiverId:DR000000001
    senderId:ECL-00000001
    receiverType:Doctor*/
    private void postCallRequestFromPatient(final String patientId) {
        String patient_login_tag = "doc_search_tag";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "saveCallRequest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("DIM", response);


                        closeDialog();

                        String APPT_AppointmentDate;
                        String InTime;
                        String InTime_AMOrPM;
                        String DRI_DrID;
                        String DRI_DrName;

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("status").equals("success")) {
                                if (!object.isNull("data")) {
                                    JSONArray array = object.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject = array.getJSONObject(i);

                                        APPT_AppointmentDate = jsonObject.getString("APPT_AppointmentDate");
                                        InTime = jsonObject.getString("InTime");
                                        InTime_AMOrPM = jsonObject.getString("InTime_AMOrPM");
                                        DRI_DrID = jsonObject.getString("DRI_DrID");
                                        DRI_DrName = jsonObject.getString("DRI_DrName");

                                        AppointmentModel model = new AppointmentModel(APPT_AppointmentDate, InTime, InTime_AMOrPM, DRI_DrID, DRI_DrName);
                                        modelList.add(model);
                                    }

                                    mAdapter.notifyDataSetChanged();
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("receiverId", patientId);
                params.put("senderId", patientId);
                params.put("receiverType", "Doctor");



                return params;
            }
        };

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }


    private void getPatientAppointment(final String patientId) {
        String patient_login_tag = "doc_search_tag";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "searchPatientAppointment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("DIM", response);


                        closeDialog();

                        String APPT_AppointmentDate;
                        String InTime;
                        String InTime_AMOrPM;
                        String DRI_DrID;
                        String DRI_DrName;

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("status").equals("success")) {
                                if (!object.isNull("data")) {
                                    JSONArray array = object.getJSONArray("data");

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject = array.getJSONObject(i);

                                        APPT_AppointmentDate = jsonObject.getString("APPT_AppointmentDate");
                                        InTime = jsonObject.getString("InTime");
                                        InTime_AMOrPM = jsonObject.getString("InTime_AMOrPM");
                                        DRI_DrID = jsonObject.getString("DRI_DrID");
                                        DRI_DrName = jsonObject.getString("DRI_DrName");

                                        AppointmentModel model = new AppointmentModel(APPT_AppointmentDate, InTime, InTime_AMOrPM, DRI_DrID, DRI_DrName);
                                        modelList.add(model);
                                    }

                                    mAdapter.notifyDataSetChanged();
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("patienId", patientId);



                return params;
            }
        };

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }
}
