package com.electrocraft.nirzo.pluse.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.electrocraft.nirzo.pluse.view.adapter.DoctorSearchListAdapter;
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

        setUpAdapter();


        getDoctorList();
//        prepareData();

        //Creating the instance of ArrayAdapter containing bloodGroupList of autoCtvHelper names
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.select_dialog_item, catName);


        actvLocationSearch.setThreshold(1);                                                         //will start working from first character
        actvLocationSearch.setAdapter(adapter);                                                     //setting the adapter data into the AutoCompleteTextView
        actvLocationSearch.setTextColor(Color.RED);
        return view;

    }

    private void setUpAdapter() {
        mAdapter = new DoctorSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDocSearch.setLayoutManager(mLayoutManager);
        rvDocSearch.setItemAnimator(new DefaultItemAnimator());
        rvDocSearch.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvDocSearch.setAdapter(mAdapter);
        rvDocSearch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvDocSearch, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DoctorSearch doctor = mList.get(position);

          /*      Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();

                Fragment fragment = new DocProfileFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();*/
                //  Timber.d("hello Doc");
              Intent intent = new Intent(getActivity(), PtSeeDoctorProfileActivity.class);
                intent.putExtra(Key.DOCTOR_NAME_KEY, doctor.getName());
                intent.putExtra(Key.KEY_DOCTOR_ID, doctor.getDrID());
                intent.putExtra(Key.KEY_DOCTOR_EXPERTISE, doctor.getExpertise());
                intent.putExtra(Key.KEY_DOCTOR_SPECIALIZATION, doctor.getSpecialization());
                intent.putExtra(Key.KEY_DOCTOR_AMOUNT, doctor.getAmount());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    private void getDoctorList() {
        String patient_login_tag = "doc_search_tag";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getdoctorslist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("DIM", response);

                        String DRI_ID = "";
                        String DRI_DrName = "";
                        String DCharge = "";
                        String Expertise = "";
                        String SPName = "";
                        String Photo = "";


                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);


                            if (!object.isNull("DoctorsLists")) {

                                JSONArray array = object.getJSONArray("DoctorsLists");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    /*
                                    "DRI_ID": "DR000000001",
            "name": "Dr. Faisal Mohammad",
            "SPName": "Cardiology",
            "Expertise": "FCPS, FRCS, MD (MED)",
            "amount": 1200
                                     */

                                    DRI_ID = jsonObject.getString("DRI_ID");
                                    DRI_DrName = jsonObject.getString("name");
                                    Expertise = jsonObject.getString("Expertise");
                                    SPName = jsonObject.getString("SPName");
                                    DCharge = jsonObject.getString("amount");
                                    Photo = jsonObject.getString("Photo");

                                    DoctorSearch doctor = new DoctorSearch(DRI_ID,DRI_DrName, Expertise, SPName, DCharge, true,Photo);
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                closeDialog();
            }
        });

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
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
