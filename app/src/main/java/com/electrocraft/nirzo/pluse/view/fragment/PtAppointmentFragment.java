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

        /*        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                startActivity(browserIntent);*/

                startActivity(new Intent(getActivity(), MainActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /**
     * {
     * "status": "success",
     * "data": [
     * {
     * "APPT_AppointmentDate": "2018-03-15",
     * "InTime": "07:00:00",
     * "InTime_AMOrPM": "PM",
     * "OutTime": "07:30:00",
     * "OutTime_AMOrPM": "PM",
     * "APPT_ShortDescriptionOfProblem": "hello ok",
     * "APPT_ApptSubmissionDate": "2018-03-15",
     * "DRI_DrID": "DR000000001",
     * "DRI_DrName": "Dr. Farzana B Ibrahim",
     * "PRI_Name": "Faisal Mohammad",
     * "PRI_PTID": "ECL-00000001",
     * "DRI_Phone": "01911444647",
     * "PRI_Phone": "1911444647"
     * }
     * ],
     * "msg": "Patient Appointment List"
     * }
     */

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
