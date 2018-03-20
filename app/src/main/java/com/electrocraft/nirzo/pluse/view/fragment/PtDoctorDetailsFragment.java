package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by nirzo on 2/26/2018.
 */

public class PtDoctorDetailsFragment extends Fragment {

    private String mDoctorId;

    @BindView(R.id.tv_specialTag)
    TextView tvSpecialTag;

    @BindView(R.id.tv_Language)
    TextView tvLanguage;

    @BindView(R.id.tvCallCharges)
    TextView tvCallCharges;

    @BindView(R.id.tvAvailableTime)
    TextView tvAvailableTime;
    private ProgressDialog pDialog;

    private String mAvailableDateString;
    private String mDocExpertise;



    public PtDoctorDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_doc_details, container, false);
        ButterKnife.bind(this, view);
        mAvailableDateString = "";
        String special = "";
        String amount = "";
        String language = "";
        Bundle arg = getArguments();
        if (arg != null) {
            special = arg.getString(Key.KEY_DOCTOR_SPECIALIZATION, "");
            amount = arg.getString(Key.KEY_DOCTOR_AMOUNT, "");
            language = arg.getString(Key.KEY_DOCTOR_LANGUAGE, "");
            mDoctorId = arg.getString(Key.KEY_DOCTOR_ID, "");
            mDocExpertise = arg.getString(Key.KEY_DOCTOR_EXPERTISE, "");
        }

        getAvailableTime(mDoctorId);
        tvSpecialTag.setText(special);
        tvLanguage.setText(language);
        tvCallCharges.setText(amount + " BDT");
        return view;
    }


    private void getAvailableTime(final String doctorId) {
        String tag = "get_doc_profile_info";

        pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getDoctorAvailableTime/" + doctorId,
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
                                    JSONArray daArray = jos.getJSONArray("data");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);

                                        String Day = object.getString("Day");
                                        String InTime = object.getString("InTime");
                                        String InTime_AMOrPM = object.getString("InTime_AMOrPM");
                                        String OutTime = object.getString("OutTime");
                                        String OutTime_AMOrPM = object.getString("OutTime_AMOrPM");
                                        String tem = Day + "    " + InTime + " " + InTime_AMOrPM + "  to  " + OutTime + " " + OutTime_AMOrPM + "\n";
                                        mAvailableDateString = mAvailableDateString + tem;
                                    }
                                    //                                   setupViewPager(viewPager);
//                                    getLanguage();
                                    tvAvailableTime.setText(mAvailableDateString);
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

    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }


}
