package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.electrocraft.nirzo.pluse.model.DoctorSearch;
import com.electrocraft.nirzo.pluse.model.Prescription;
import com.electrocraft.nirzo.pluse.view.activity.patient.PtSeeDoctorProfileActivity;
import com.electrocraft.nirzo.pluse.view.adapter.DoctorSearchListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.PrescriptionListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.RecyclerTouchListener;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on 3/25/2018.
 */

public class PtPescriptionListFrag extends Fragment {

    private PrescriptionListAdapter mAdapter;
    private List<Prescription> mList = new ArrayList<>();

    @BindView(R.id.recyPres)
    RecyclerView recyclerView;
    private ProgressDialog pDialog;

    public PtPescriptionListFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_s_prescription_list, container, false);
        ButterKnife.bind(this, view);
        setUpAdapter();

        getAllDoctorList();
        return view;
    }

    private void setUpAdapter() {
        mAdapter = new PrescriptionListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Bundle arg = new Bundle();
                arg.putString("pescription_id", mList.get(position).getPI_DoctorPrescriptionCode());

                /*Fragment fragment = new PatientPrescriptionFragment();
                if (fragment != null) {

                    //set Bundle
                    fragment.setArguments(arg);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();ft.addToBackStack(null);
                    ft.add(R.id.content_frame, fragment);
                    ft.commit();
                }*/

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), Uri.parse("http://docs.google.com/gview?embedded=true&url=http://180.148.210.139:8081/pulse_api/api/prescriptionpdf/"+ mList.get(position).getPI_DoctorPrescriptionCode()));
                builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//                DoctorSearch doctor = mList.get(position);


         /*       Intent intent = new Intent(getActivity(), PtSeeDoctorProfileActivity.class);
                intent.putExtra(Key.DOCTOR_NAME_KEY, doctor.getName());
                intent.putExtra(Key.KEY_DOCTOR_ID, doctor.getDrID());
                intent.putExtra(Key.KEY_DOCTOR_EXPERTISE, doctor.getExpertise());
                intent.putExtra(Key.KEY_DOCTOR_SPECIALIZATION, doctor.getSpecialization());
                intent.putExtra(Key.KEY_DOCTOR_AMOUNT, doctor.getAmount());
                startActivity(intent);
*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getAllDoctorList() {
        String patient_login_tag = "doc_all_search_tag";
        if (pDialog == null)
            pDialog = new ProgressDialog(getActivity());
        //pDialog.setMessage("Loading...");
        //pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getprescriptionCodeList/" + AppSharePreference.getPatientID(getActivity()) + "/1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("DIM", response);

               /*         "PRI_PTID":"ECL-00000001",
                                "DRI_DrID":"DR000000001",
                                "PI_DoctorPrescriptionCode":"DP-00000018",
                                "CI_ConsultationDate":"2018-03-15",
                                "DRI_DrName":"Dr. Farzana B Ibrahim"*/

                        String PRI_PTID = "";
                        String DRI_DrID = "";
                        String PI_DoctorPrescriptionCode = "";
                        String CI_ConsultationDate = "";
                        String DRI_DrName = "";


                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);


                            if (!object.isNull("data")) {

                                JSONArray array = object.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    PRI_PTID = jsonObject.getString("PRI_PTID");
                                    DRI_DrID = jsonObject.getString("DRI_DrID");
                                    PI_DoctorPrescriptionCode = jsonObject.getString("PI_DoctorPrescriptionCode");
                                    CI_ConsultationDate = jsonObject.getString("CI_ConsultationDate");
                                    DRI_DrName = jsonObject.getString("DRI_DrName");


                                    Prescription doctor = new Prescription(PRI_PTID, DRI_DrID, PI_DoctorPrescriptionCode, CI_ConsultationDate, DRI_DrName);
                                    mList.add(doctor);
                                }

                                mAdapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        });

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

    }
}
