package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
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
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static com.electrocraft.nirzo.pluse.view.fragment.PtAppointBookReasonFragment.selectedDateString;
import static com.electrocraft.nirzo.pluse.view.fragment.PtAppointBookReasonFragment.selectedDateTime;
import static com.electrocraft.nirzo.pluse.view.fragment.PtAppointBookReasonFragment.selectedTimeString;

/**
 * Created by nirzo on 3/17/2018.
 */

public class PtPaymentModuleFragment extends Fragment {

    @BindView(R.id.civ_doc_profile_image)
    CircleImageView civDoctorImage;


    @BindView(R.id.spMobilePaymentWay)
    Spinner spMobilePaymentWay;

    @BindView(R.id.tvDocName)
    TextView tvDocName;

    @BindView(R.id.tvDocDegree)
    TextView tvDocDegree;

    @BindView(R.id.tvAppointDate)
    TextView tvAppointDate;


    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;


    @BindView(R.id.btnProcessToPayment)
    Button btnProcessToPayment;

    private String mDoctorId;
    private String mShortDescOfProblem;
    private String mPatientId;
    private String mAppointmentDate;
    private String mAppointmentTime;
    private String mOat;
    private ProgressDialog pDialog;
    private String mDocExpertise;
    private String mDocAmount;

    public PtPaymentModuleFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_payment_module, container, false);
        ButterKnife.bind(this, view);


        Bundle arg = getArguments();
        if (arg != null) {
            mDoctorId = arg.getString(Key.KEY_DOCTOR_ID, "");
            mPatientId = arg.getString(Key.KEY_PATIENT_ID, "");
            mShortDescOfProblem = arg.getString(Key.KEY_PATIENT_PROBLEM_SHORT_DES, "");
            mAppointmentDate = arg.getString(Key.KEY_APPOINTMENT_DATE, "");
            mAppointmentTime = arg.getString(Key.KEY_APPOINTMENT_TIME, "");
            mOat = arg.getString(Key.KEY_OAT, "");
            mDocExpertise = arg.getString(Key.KEY_DOCTOR_EXPERTISE, "");
            mDocAmount = arg.getString(Key.KEY_DOCTOR_AMOUNT, "");


        }
        getDoctorProfile(mDoctorId);
        loadMobilePaymentWay();
        return view;
    }

    @OnClick(R.id.btnProcessToPayment)
    public void paymentProccide() {

        setTrangectionID();
      /*  saveDoctorAppointmentTime(mPatientId, mDoctorId, mAppointmentDate, mOat, "", "",
                "0", mShortDescOfProblem, "0",
                "", "", "", "", "");*/
    }

    private void setTrangectionID() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Transaction ID");
        alertDialog.setMessage("Enter Insert your transaction ID");

        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
//        final EditText input = new EditText(getActivity());
        alertDialog.setIcon(R.drawable.ic_account_balance);
        final String pass = "123";
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();

                        if (pass.equals(password)) {
                            saveDoctorAppointmentTime(mPatientId, mDoctorId, mAppointmentDate, mOat, "", "",
                                    "0", mShortDescOfProblem, "0",
                                    "", "", "", "", "");


                        } else {
                            Toast.makeText(getActivity(), "Please input correct transaction ID ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    /**
     * todo: save Doctor AppointmentTime after save
     *
     * @param patient_id patient id
     */
    private void saveDoctorAppointmentTime(final String patient_id
            , final String doctor_id, final String APPT_AppointmentDate
            , final String oat_cod, final String PPFI_Code
            , final String rel_Code, final String DrChargePaymentComplete
            , final String APPT_ShortDescriptionOfProblem, final String hasDiabetics
            , final String hasHighBloodPressure, final String hasAllergies
            , final String hasAnyMedication, final String curMedicineList
            , final String APPT_Comments) {


        String tag = "save_appointment";

      /*  pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Saving...");*/
        if (pDialog != null)
            pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "saveDoctorAppointmentinfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("SAVE_MOR", response);
                        closeDialog();
                        /**
                         * {
                         "status": "success",
                         "data": {
                         "id": "APPT-00000002"
                         },
                         "msg": "AppointmentCode Information  successfully Save"
                         }


                         */

                        String id = "";
                        String msg = "";

                        try {
                            JSONObject jos = new JSONObject(response);

                            msg = jos.getString("msg");
                            if (jos.getString("status").equals("success")) {
                                JSONObject data = jos.getJSONObject("data");
                                id = data.getString("id");
                                AppSharePreference.saveAppointmentID(getActivity(), id);

                                //success
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setMessage("Your payment has been successful.");


                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                selectedDateTime = "";
                                                selectedTimeString = "";
                                                selectedDateString = "";
                                                getActivity().finish();
                                                dialog.cancel();
                                                //todo finish the appoint fragment activity
                                                Intent intent= new Intent(getActivity(), PatientHomeActivity.class);
                                                intent.putExtra("redirfrom",true);
                                                startActivity(intent);
                                              /*  Fragment fragment = new PtAppointmentFragment();
                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                ft.addToBackStack(null);
                                                ft.replace(R.id.content_frame, fragment);
                                                ft.commit();*/

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.setCancelable(false);
                                alert11.show();

                            } else {
                                //failed
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setMessage("Your payment has been unsuccessful.");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                selectedDateTime = "";
                                                selectedTimeString = "";
                                                selectedDateString = "";
                                                dialog.cancel();
                                                //todo finish the previous activity

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.setCancelable(false);
                                alert11.show();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("patient_id", patient_id);
                params.put("doctor_id", doctor_id);
                params.put("APPT_AppointmentDate", APPT_AppointmentDate);
                params.put("oat_cod", oat_cod);
                params.put("PPFI_Code", PPFI_Code);
                params.put("rel_Code", rel_Code);
                params.put("DrChargePaymentComplete", DrChargePaymentComplete);
                params.put("APPT_ShortDescriptionOfProblem", APPT_ShortDescriptionOfProblem);
                params.put("hasDiabetics", hasDiabetics);
                params.put("hasHighBloodPressure", hasHighBloodPressure);
                params.put("hasAllergies", hasAllergies);
                params.put("hasAnyMedication", hasAnyMedication);
                params.put("curMedicineList", curMedicineList);
                params.put("APPT_Comments", APPT_Comments);


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void loadMobilePaymentWay() {
        List<SpinnerHelper> list = new ArrayList<>();
        SpinnerHelper helper;
        helper = new SpinnerHelper(0, "001", "Select payment type");
        list.add(helper);
        helper = new SpinnerHelper(1, "001", "Bkash");
        list.add(helper);
        helper = new SpinnerHelper(2, "002", "Sure Cash");
        list.add(helper);
        helper = new SpinnerHelper(3, "003", "Rocket");
        list.add(helper);

        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getContext(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spMobilePaymentWay.setAdapter(adapter);

        spMobilePaymentWay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strCountry = ((SpinnerHelper) spMobilePaymentWay.getSelectedItem()).getDatabaseValue();
                String valueCountryCode = ((SpinnerHelper) spMobilePaymentWay.getSelectedItem()).getDatabaseId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getDoctorProfile(final String doctorId) {
        String tag = "get_doc_profile_info";

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
                       /* {
    "status": "success",
    "data": [
        [
            {
                "DrProfilePersonalInfoCode": "DPPI_005",
                "DRI_DrID": "DR-00000005",
                "FirstName": "Ahsan",
                "LastName": "Ahmed",
                "DOB": "2078-03-12 00:00:00",
                "Age": null,
                "NI_NationalityCode": "NL001",
                "BG_BloodGroupCode": "BG002",
                "SLI_LanguageName": "L002",
                "Photo": "DR-0000000521521025768.png",
                "Signature": null,
                "BMDCNo": null,
                "DrPassport": null,
                "DrNID": null,
                "DrMedicalLisence": null,
                "NoOfVote": null,
                "PercentOfRateForVote": null,
                "RegularConsultationCharge": 1000,
                "DaysLimitForAppointment": 7,
                "DaysLimitFor2ndAppointment": 2,
                "DiscountRatioOfRegConsulCharge": "30",
                "DRI_EntryBy": "Admin",
                "DRI_EntryDate": "2018-03-13 00:00:00",
                "DRI_ModifyBy": null,
                "DRI_ModifyDate": null
            }
        ]
    ],
    "msg": "Patien Health Information"
}*/

                        String FirstName = "";
                        String LastName = "";
                        String Photo = "";
                        try {
                            JSONObject jos = new JSONObject(response);

                            if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONArray daArray = jos.getJSONArray("data");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);

                                        FirstName = object.getString("FirstName");
                                        LastName = object.getString("LastName");
                                        Photo = object.getString("Photo");
                                    }

                                    getDoctorImageRequest(Photo);

                                    textToView(FirstName + " " + LastName);
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

    private void textToView(String name) {
        tvDocName.setText(name);
        tvDocDegree.setText(mDocExpertise);
        tvAppointDate.setText(mAppointmentDate + " time (" + mAppointmentTime + ")");
        tvTotalAmount.setText(tvTotalAmount.getText().toString() + " " + mDocAmount + " BDT");
    }


    private void getDoctorImageRequest(String imageLink) {

       /* pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");*/
        pDialog.show();

//        String url = "http://180.148.210.139:8081/pulse_api/public/Doctor_profile_photo/";


        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(AppConfig.LIVE_IMAGE_DOCTOR_API_LINK + imageLink,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        civDoctorImage.setImageBitmap(bitmap);
                        pDialog.hide();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        civDoctorImage.setImageResource(R.drawable.ic_doctor);
                        pDialog.hide();
                    }
                });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request);
    }


    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

}
