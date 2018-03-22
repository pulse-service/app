package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.model.AppointmentModel;
import com.electrocraft.nirzo.pluse.view.MainActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorPrescription;
import com.electrocraft.nirzo.pluse.view.adapter.DoctorsAppointmentListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.RecyclerTouchListener;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on 3/17/2018.
 */

public class DocAppointmentFragment extends Fragment {

    @BindView(R.id.recyDocAppointment)
    RecyclerView rcvAppointment;

    List<AppointmentModel> modelList = new ArrayList<>();

    private DoctorsAppointmentListAdapter mAdapter;

    /* @BindView(R.id.lr_btn_Call)
     Button lr_btn_Call;*/
    private String mPatientId;
    private String mPatientAppointDate;
    private ProgressDialog pDialog;
    private String TAG = "DocAppointmentFragment";

    public DocAppointmentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_s_appointment_list, container, false);
        ButterKnife.bind(this, view);
        setUpAdapter();

        Date c = Calendar.getInstance().getTime();
//        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        String mDoctorId = AppSharePreference.getDoctorID(getActivity());
        getDoctorAppointment(mDoctorId, formattedDate);
        return view;
    }

    private void setUpAdapter() {
        mAdapter = new DoctorsAppointmentListAdapter(modelList, new DoctorsAppointmentListAdapter.EditClickListener() {
            @Override
            public void setEditClickListener(int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);

                intent.putExtra("appointCode", "APPT-00000001");
                intent.putExtra("patientId", modelList.get(position).getPatientID());

                startActivity(intent);
                // call
            }
        }, new DoctorsAppointmentListAdapter.EditClickListener1() {
            @Override
            public void setEditClickListener(int position) {
                Intent intent = new Intent(getActivity(), DoctorPrescription.class);
//                intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);
                startActivity(intent);
                // pescription
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rcvAppointment.setLayoutManager(mLayoutManager);
        rcvAppointment.setItemAnimator(new DefaultItemAnimator());
        rcvAppointment.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rcvAppointment.setAdapter(mAdapter);

       /* rcvAppointment.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rcvAppointment, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    */
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
     * "PRI_PTName": "Faisal Mohammad",
     * "DRI_Phone": "01911444647",
     * "PRI_Phone": "1911444647"
     * }
     * ],
     * "msg": "Doctor Appointment List"
     * }
     */
    private void getDoctorAppointment(final String doctorId, final String date) {
        String patient_login_tag = "doctor_s_appointment";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "searchDoctorAppointment"
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("DIM", response);


                        closeDialog();

                        String APPT_AppointmentDate;
                        String InTime;
                        String InTime_AMOrPM;
                        String PRI_PTID;
                        String PRI_Name;
                        String APPT_ShortDescriptionOfProblem;
                        String DRI_DrID;

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
                                        PRI_PTID = jsonObject.getString("PRI_PTID");
                                        PRI_Name = jsonObject.getString("PRI_Name");
                                        APPT_ShortDescriptionOfProblem = jsonObject.getString("APPT_ShortDescriptionOfProblem");
                                        DRI_DrID = jsonObject.getString("DRI_DrID");

                                        AppointmentModel model = new AppointmentModel(APPT_AppointmentDate, InTime, InTime_AMOrPM, PRI_PTID, PRI_Name, APPT_ShortDescriptionOfProblem);
                                        model.setDoctorID(DRI_DrID);
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


                params.put("doctorId", doctorId);
                params.put("date", date);


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
