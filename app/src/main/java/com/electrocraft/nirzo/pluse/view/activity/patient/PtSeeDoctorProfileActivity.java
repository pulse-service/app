package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.activity.patient.PtAppointBookActivity;
import com.electrocraft.nirzo.pluse.view.adapter.ViewPagerAdapter;
import com.electrocraft.nirzo.pluse.view.fragment.PtDocBlogFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtDocServiceDetailsFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtDoctorDetailsFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class PtSeeDoctorProfileActivity extends AppCompatActivity {


    /*public PtSeeDoctorProfileActivity() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_view_doctor_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }*/

    private OnAboutDataRecivelistener mDataRecivelistener;
    private ProgressDialog pDialog;
    private String SLI_LanguageName;
    private String Photo;
    private String mDoctorName;

    public interface OnAboutDataRecivelistener {
        void onDataRecived(String string);
    }

    public void setAboutDataListener(OnAboutDataRecivelistener listener) {
        this.mDataRecivelistener = listener;
    }

    private String doctorId;
    private String mDocExpertise;
    private String docSpecialization;
    private String mDocAmount;

    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tvDocName)
    TextView tvDocName;

    @BindView(R.id.ivDoctorCoverPic)
    ImageView ivDocCoverPic;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //    private boolean reDirEnable = false;
    List<SpinnerHelper> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_pt_view_doctor_profile);
        ButterKnife.bind(this);
        doctorId = getIntent().getStringExtra(Key.KEY_DOCTOR_ID);
        getDoctorProfile(doctorId);

         mDoctorName = getIntent().getStringExtra(Key.DOCTOR_NAME_KEY);

        mDocExpertise = getIntent().getStringExtra(Key.KEY_DOCTOR_EXPERTISE);
        docSpecialization = getIntent().getStringExtra(Key.KEY_DOCTOR_SPECIALIZATION);
        mDocAmount = getIntent().getStringExtra(Key.KEY_DOCTOR_AMOUNT);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {                                    // safety block
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        tabLayout.setupWithViewPager(viewPager);

//        reDirEnable = false;


        tvDocName.setText(mDoctorName);
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle arg = new Bundle();
        arg.putString(Key.KEY_DOCTOR_SPECIALIZATION, docSpecialization);
        arg.putString(Key.KEY_DOCTOR_AMOUNT, mDocAmount);
        arg.putString(Key.KEY_DOCTOR_ID, doctorId);
        arg.putString(Key.KEY_DOCTOR_LANGUAGE, SLI_LanguageName);
        arg.putString(Key.DOCTOR_NAME_KEY, mDoctorName);
        arg.putString(Key.KEY_DOCTOR_EXPERTISE, mDocExpertise);

//        Fragment fragment = PtDoctorDetailsFragment.newInstance(docSpecialization);
        Fragment fragment = new PtDoctorDetailsFragment();
        fragment.setArguments(arg);
        adapter.addFragment(fragment,
                getResources().getString(R.string.details));

        adapter.addFragment(new PtDocServiceDetailsFragment(),
                getResources().getString(R.string.service));

        adapter.addFragment(new PtDocBlogFragment(),
                getResources().getString(R.string.blog));

        viewPager.setAdapter(adapter);

    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (!reDirEnable)
//            finish();
    }


    @OnClick(R.id.btn_appointBook)
    public void onAppointmentBookClick() {


        Intent intent = new Intent(this, PtAppointBookActivity.class);
        intent.putExtra(Key.KEY_DOCTOR_ID, doctorId);
        intent.putExtra(Key.KEY_DOCTOR_EXPERTISE, mDocExpertise);
        intent.putExtra(Key.KEY_DOCTOR_AMOUNT, mDocAmount);

        startActivity(intent);
    }


    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

    private void getDoctorProfile(final String doctorId) {
        String tag = "get_doc_profile_info";

        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
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

                        try {
                            JSONObject jos = new JSONObject(response);

                            if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONArray daArray = jos.getJSONArray("data");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);

                                        SLI_LanguageName = object.getString("SLI_LanguageName");
                                        Photo = object.getString("Photo");


                                    }
                                    setupViewPager(viewPager);

                                    getDoctorImageRequest(Photo);
//                                    getLanguage();
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
                        ivDocCoverPic.setImageBitmap(bitmap);
                        pDialog.hide();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        ivDocCoverPic.setImageResource(R.drawable.ic_doctor);
                        pDialog.hide();
                    }
                });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request);
    }


   /* private void getLanguage() {
        String blood_group_tag = "language_tag";
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getlanguagelist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Timber.d(response);

                        String lanCode = "";
                        String lanName = "";

                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);

                            if (!object.isNull("data")) {

                                JSONArray array = object.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    lanCode = jsonObject.getString("lanCode");
                                    lanName = jsonObject.getString("lanName");

                                    SpinnerHelper helper = new SpinnerHelper(i, lanCode, lanName);
                                    languageList.add(helper);

                                }
//                                loadLanguage(languageList);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                closeDialog();                                                                           // hide the progress dialog
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, blood_group_tag);

    }*/
}
