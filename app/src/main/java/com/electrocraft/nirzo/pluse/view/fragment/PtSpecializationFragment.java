package com.electrocraft.nirzo.pluse.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;
import com.electrocraft.nirzo.pluse.view.activity.LoginActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PtDoctorProfileActivity;
import com.electrocraft.nirzo.pluse.view.adapter.DoctorSearchListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.RecyclerTouchListener;
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

/**
 * @author Faisal Mohammad
 * @since 2/20/2018
 */

public class PtSpecializationFragment extends Fragment {

    private static final String TAG = "PtSpecializationFragment";

    //    @BindView(R.otpCode.sp_specializationCat)
//    Spinner spSpecialCat;
    private String[] catName = {"Dentist", "General Physician", "Homeopathy", "Orthopedist"};
    private String[] catCode = {"01", "02", "03", "04"};
    @BindView(R.id.recyVDocSearch)
    RecyclerView rvDocSearch;
    private List<DoctorSearch> mList = new ArrayList<>();


    private DoctorSearchListAdapter mAdapter;

    @BindView(R.id.actv_specializationCat)
    AutoCompleteTextView actvLocationSearch;
    private String mToken = "";
    private ProgressDialog pDialog;

    //String [] autoCtvHelper = {};


    public PtSpecializationFragment() {
        // required  empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_specialization, container, false);
        ButterKnife.bind(this, view);

//        loadCategories();

        mAdapter = new DoctorSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDocSearch.setLayoutManager(mLayoutManager);
        rvDocSearch.setItemAnimator(new DefaultItemAnimator());
        rvDocSearch.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvDocSearch.setAdapter(mAdapter);
        rvDocSearch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvDocSearch, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DoctorSearch doctorSearch = mList.get(position);
                //  Timber.d("hello Doc");
                Intent intent = new Intent(getActivity(), PtDoctorProfileActivity.class);
                intent.putExtra(Key.DOCTOR_NAME_KEY, doctorSearch.getName());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getToken();
//        prepareData();

        //Creating the instance of ArrayAdapter containing bloodGroupList of autoCtvHelper names
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.select_dialog_item, catName);


        actvLocationSearch.setThreshold(1);                                                         //will start working from first character
        actvLocationSearch.setAdapter(adapter);                                                     //setting the adapter data into the AutoCompleteTextView
        actvLocationSearch.setTextColor(Color.RED);
        return view;

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
                    mToken = jsonObject.getString("token");
                    if (mToken.length() > 20)
                        getDoctorList(mToken);
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


    private void getDoctorList(final String token) {
        String patient_login_tag = "patient_log_in_tag";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.API_LINK + "doctorsList" + "?token=" + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("DIM", response);

                String name = "";
                String amount = "";


                pDialog.hide();
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getBoolean("flag")) {
                        if (!object.isNull("DoctorsLists")) {

                            JSONArray array = object.getJSONArray("DoctorsLists");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                name = jsonObject.getString("name");

                                amount = jsonObject.getString("amount");

                                DoctorSearch doctor = new DoctorSearch(name, " ", amount, true);
                                mList.add(doctor);
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
                pDialog.hide();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }

    /*
     * load spinner of Doctor's Specialization
     */
   /* private void loadCategories() {
        List<SpinnerHelper> bloodGroupList = new ArrayList<>();
        for (int i = 0; i < catName.length; i++) {
            SpinnerHelper helper = new SpinnerHelper(i, catCode[i], catName[i]);
            bloodGroupList.add(helper);
        }
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, bloodGroupList);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spSpecialCat.setAdapter(adapter);
    }*/
}
