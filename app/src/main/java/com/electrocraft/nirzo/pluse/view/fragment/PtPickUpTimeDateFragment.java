package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.model.DoctorAvailableTime;
import com.electrocraft.nirzo.pluse.view.util.Key;
import com.electrocraft.nirzo.pluse.view.viewhelper.BKViewController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import timber.log.Timber;

/**
 * Created by nirzo on 2/26/2018.
 */

public class PtPickUpTimeDateFragment extends Fragment {

    private List<DoctorAvailableTime> mList = new ArrayList<>();

    String mCurrentMonth = "";
    String mCurrentYear = "";
    String mToday = "";

    List<String> autoCtvHelper = new ArrayList<>();

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.btn_pt_doc_PicTime_7)
    Button btn_pt_doc_PicTime_7;

    @BindView(R.id.btn_pt_doc_PicTime_8)
    Button btn_pt_doc_PicTime_8;

    @BindView(R.id.btn_pt_doc_PicTime_9)
    Button btn_pt_doc_PicTime_9;

    @BindView(R.id.btn_pt_doc_PicTime_6)
    Button btn_pt_doc_PicTime_6;

    @BindView(R.id.btn_pt_doc_PicTime_5)
    Button btn_pt_doc_PicTime_5;

    @BindView(R.id.btn_pt_doc_PicTime_4)
    Button btn_pt_doc_PicTime_4;

    @BindView(R.id.btn_pt_doc_PicTime_3)
    Button btn_pt_doc_PicTime_3;


    @BindView(R.id.btn_pt_doc_PicTime_2)
    Button btn_pt_doc_PicTime_2;


    @BindView(R.id.btn_pt_doc_PicTime_1)
    Button btn_pt_doc_PicTime_1;


    private ProgressDialog pDialog;
    private String mDoctorId;
    private String mOAT_code;
    private String mPatientId;
    private String mShortDescriptionOfProblem;
    private String mDocExpertise;
    private String mDocAmount;


    public PtPickUpTimeDateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_pickup_calander, container, false);
        ButterKnife.bind(this, view);

        disableAllButton();

        Bundle arg = getArguments();
        if (arg != null) {
            mDoctorId = arg.getString(Key.KEY_DOCTOR_ID, "");
            mShortDescriptionOfProblem = arg.getString(Key.KEY_PATIENT_PROBLEM_SHORT_DES, "");
            mDocExpertise = arg.getString(Key.KEY_DOCTOR_EXPERTISE, "");
            mDocAmount = arg.getString(Key.KEY_DOCTOR_AMOUNT, "");


        }


        /**
         * get patient ID
         */
        mPatientId = AppSharePreference.getPatientID(getActivity());

        getAvailableTime(mDoctorId);
    /*    if (mDoctorId!=null && mDoctorId.length()>0)
            Toast.makeText(getActivity(),"Doctor id"+mDoctorId,Toast.LENGTH_SHORT).show();*/

        getCurrentDate();
     /*   Calendar febFirst = Calendar.getInstance();

       febFirst.set(2018, 3, 1, 0, 0, 0);

        calendarView.setMinDate(febFirst.getTimeInMillis());

        Calendar febLast = Calendar.getInstance();


        febLast.set(2018, 3, 0, 0, 0, 0);

        calendarView.setMaxDate(febLast.getTimeInMillis());
        calendarView.setDate(febFirst.getTimeInMillis());*/

        setUpCalender();



  /*      mAdapter = new LocationSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvLocationSearch.setLayoutManager(mLayoutManager);
        rvLocationSearch.setItemAnimator(new DefaultItemAnimator());
        rvLocationSearch.setAdapter(mAdapter);
        prepareData();*/
        return view;
    }

    public void disableAllButton() {

        goneButton(btn_pt_doc_PicTime_1);
        goneButton(btn_pt_doc_PicTime_2);
        goneButton(btn_pt_doc_PicTime_3);
//        goneButton(btn_pt_doc_PicTime_08_30);
        goneButton(btn_pt_doc_PicTime_4);
        goneButton(btn_pt_doc_PicTime_5);
        goneButton(btn_pt_doc_PicTime_6);
        goneButton(btn_pt_doc_PicTime_7);
        goneButton(btn_pt_doc_PicTime_8);
        goneButton(btn_pt_doc_PicTime_9);

    }

    private void goneButton(View view) {

        ButterKnife.apply(view, BKViewController.GONE);
    }


    private void visibleButton(View view) {

        ButterKnife.apply(view, BKViewController.VISIBLE);
    }


    private void setUpCalender() {

    }

    private void getCurrentDate() {
        String tag = "get_current_date_month_year";

   /*     pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");*/
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getdate",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("MOR", response);
                        closeDialog();

                        try {
                            JSONObject jos = new JSONObject(response);

                            if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONObject object = jos.getJSONObject("data");

                                    if (!object.isNull("Month")) {

                                        JSONArray monthArray = object.getJSONArray("Month");

                                        for (int i = 0; i < monthArray.length(); i++) {
                                            JSONObject monthObject = monthArray.getJSONObject(i);
                                            mCurrentMonth = monthObject.getString("Month");
                                        }

                                    }

                                    if (!object.isNull("Year")) {

                                        JSONArray yearArray = object.getJSONArray("Year");

                                        for (int i = 0; i < yearArray.length(); i++) {
                                            JSONObject yearObject = yearArray.getJSONObject(i);
                                            mCurrentYear = yearObject.getString("Year");
                                        }

                                    }

                                    if (!object.isNull("Today")) {

                                        JSONArray array = object.getJSONArray("Today");

                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsonObject = array.getJSONObject(i);
                                            mToday = jsonObject.getString("toDay");
                                        }
                                    }
                                }
                            }
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

    private void getAvailableTime(final String doctorId) {
        String tag = "get_doc_available_time";
        if (pDialog == null)
            pDialog = new ProgressDialog(getActivity());

        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getdoctorProfileView/" + doctorId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("MOR", response);
                        closeDialog();

                        try {
                            JSONObject jos = new JSONObject(response);

                            if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("AvailableTime")) {
                                    JSONArray daArray = jos.getJSONArray("AvailableTime");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);

                                        String Day = object.getString("Day");
                                        String InTime = object.getString("InTime");
                                        String InTime_AMOrPM = object.getString("InTime_AMOrPM");
                                        String OutTime = object.getString("OutTime");
                                        String OutTime_AMOrPM = object.getString("OutTime_AMOrPM");
                                        String OAT_COD = object.getString("OAT_COD");

                                        DoctorAvailableTime availableTime = new DoctorAvailableTime(Day, InTime, InTime_AMOrPM, OutTime, OutTime_AMOrPM, OAT_COD);
                                        mList.add(availableTime);
                                    }

                                    setUpButton();

                                }

                            }


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

    private void setUpButton() {
        int i = 0;
        for (DoctorAvailableTime time : mList) {

            String inTime = time.getInTime().substring(0, 5) + " " + time.getInTime_AMOrPM();
            switch (i) {
                case 0:
                    btn_pt_doc_PicTime_1.setTag(0);
                    btn_pt_doc_PicTime_1.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_1);
                    break;
                case 1:
                    btn_pt_doc_PicTime_2.setTag(1);
                    btn_pt_doc_PicTime_2.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_2);
                    break;
                case 2:
                    btn_pt_doc_PicTime_3.setTag(2);
                    btn_pt_doc_PicTime_3.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_3);
                    break;
                case 3:
                    btn_pt_doc_PicTime_4.setTag(3);
                    btn_pt_doc_PicTime_4.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_4);
                    break;
                case 4:
                    btn_pt_doc_PicTime_5.setTag(4);
                    btn_pt_doc_PicTime_5.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_5);
                    break;
                case 5:
                    btn_pt_doc_PicTime_6.setTag(5);
                    btn_pt_doc_PicTime_6.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_6);
                    break;

                case 6:
                    btn_pt_doc_PicTime_7.setTag(6);
                    btn_pt_doc_PicTime_7.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_7);
                    break;
                case 7:
                    btn_pt_doc_PicTime_8.setTag(7);
                    btn_pt_doc_PicTime_8.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_8);
                    break;
                case 8:
                    btn_pt_doc_PicTime_9.setTag(8);
                    btn_pt_doc_PicTime_9.setText(inTime);
                    visibleButton(btn_pt_doc_PicTime_9);
                    break;

            }
            i++;
        }


    }

    @Optional
    @OnClick({R.id.btn_pt_doc_PicTime_1, R.id.btn_pt_doc_PicTime_2, R.id.btn_pt_doc_PicTime_3,
            R.id.btn_pt_doc_PicTime_4, R.id.btn_pt_doc_PicTime_5, R.id.btn_pt_doc_PicTime_6,
            R.id.btn_pt_doc_PicTime_7, R.id.btn_pt_doc_PicTime_8, R.id.btn_pt_doc_PicTime_9})
    public void confirmAppointment(View view) {

        Button button = (Button) view;
        mOAT_code = mList.get((int) button.getTag()).getOat_code();
        String ti = (String) button.getText();
        showConfirmMessage(ti);
    }

    private void showConfirmMessage(final String time) {
        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .create();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedDate = sdf.format(new Date(calendarView.getDate()));
        ad.setCancelable(false);
        ad.setTitle("You have Selected");
        ad.setMessage("Your Date :" + selectedDate + "\n time : " + time);
        ad.setButton("Confirm", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();


                Fragment fragment = new PtPaymentModuleFragment();
                Bundle arg = new Bundle();
                arg.putString(Key.KEY_DOCTOR_ID, mDoctorId);
                arg.putString(Key.KEY_PATIENT_ID, mPatientId);
                arg.putString(Key.KEY_PATIENT_PROBLEM_SHORT_DES, mShortDescriptionOfProblem);
                arg.putString(Key.KEY_APPOINTMENT_DATE, selectedDate);
                arg.putString(Key.KEY_APPOINTMENT_TIME, time);
                arg.putString(Key.KEY_OAT, mOAT_code);
                arg.putString(Key.KEY_DOCTOR_EXPERTISE, mDocExpertise);
                arg.putString(Key.KEY_DOCTOR_AMOUNT, mDocAmount);
                fragment.setArguments(arg);
/*          *//*      String shortDescribtion=edtShortDescribtion.getText().toString();
*//*
//                if (mDoctorId!=null &&mDoctorId.length()>0){


                }*/
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

           /*     saveDoctorAppointmentTime(mPatientId, mDoctorId, selectedDate, mOAT_code, "", "",
                        "0", mShortDescriptionOfProblem, "0",
                        "", "", "", "", "");*/
            }
        });
        ad.show();
    }


    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

}
