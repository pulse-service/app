package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.electrocraft.nirzo.pluse.view.adapter.ViewPagerAdapter;
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
    private String SLI_LanguageCode;
    private String Photo;

    public interface OnAboutDataRecivelistener {
        void onDataRecived(String string);
    }

    public void setAboutDataListener(OnAboutDataRecivelistener listener) {
        this.mDataRecivelistener = listener;
    }

    private String doctorId;
    private String docExpertise;
    private String docSpecialization;
    private String docAmount;

    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tvDocName)
    TextView tvDocName;

    @BindView(R.id.ivDoctorCoverPic)
    ImageView ivDocCoverPic;

    private boolean reDirEnable = false;
    List<SpinnerHelper> languageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_pt_view_doctor_profile);
        ButterKnife.bind(this);
        doctorId = getIntent().getStringExtra(Key.KEY_DOCTOR_ID);
        getDoctorProfile(doctorId);

        String doctorName = getIntent().getStringExtra(Key.DOCTOR_NAME_KEY);

        docExpertise = getIntent().getStringExtra(Key.KEY_DOCTOR_EXPERTISE);
        docSpecialization = getIntent().getStringExtra(Key.KEY_DOCTOR_SPECIALIZATION);
        docAmount = getIntent().getStringExtra(Key.KEY_DOCTOR_AMOUNT);


        tabLayout.setupWithViewPager(viewPager);

        reDirEnable = false;


        tvDocName.setText(doctorName);
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle arg = new Bundle();
        arg.putString(Key.KEY_DOCTOR_SPECIALIZATION, docSpecialization);
        arg.putString(Key.KEY_DOCTOR_AMOUNT, docAmount);
        arg.putString(Key.KEY_DOCTOR_ID, doctorId);
        arg.putString(Key.KEY_DOCTOR_LANGUAGE, SLI_LanguageCode);

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
    public void onAppointmentBookClick(View view) {
        reDirEnable = true;
//        startActivity(new Intent(PtSeeDoctorProfileActivity.this, PtAppointBookActivity.class));
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

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getDoctorProfilePersonalinfo/" + doctorId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("MOR", response);
                        closeDialog();
                       /* "DrProfilePersonalInfoCode": "DPPI-00000008",
                                "DRI_DrID": "DR-000000052",
                                "FirstName": "ahsan",
                                "LastName": "habib",
                                "DOB": "2018-03-14 00:00:00",
                                "Age": 24,
                                "NI_NationalityCode": "NL001",
                                "BG_BloodGroupCode": "BG001",
                                "SLI_LanguageCode": "L002",
                                "Photo": "DR-0000000521521025768.png",
                                "Signature": "DR-0000000521521025768.PNG",
                                "BMDCNo": "12345",
                                "DrPassport": "DR-0000000521521025769.PNG",
                                "DrNID": "DR-0000000521521025769.PNG",
                                "DrMedicalLisence": "DR-0000000521521025769.PNG",
                                "NoOfVote": 2,
                                "PercentOfRateForVote": 0,
                                "RegularConsultationCharge": 1000,
                                "DaysLimitForAppointment": 1200,
                                "DaysLimitFor2ndAppointment": 600,
                                "DiscountRatioOfRegConsulCharge": "20",*/

                        try {
                            JSONObject jos = new JSONObject(response);

                            if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONArray daArray = jos.getJSONArray("data");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);

                                        SLI_LanguageCode = object.getString("SLI_LanguageCode");
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

    private void getDoctorImageRequest(String link) {

       /* pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");*/
        pDialog.show();

        String url = "http://180.148.210.139:8081/pulse_api/public/Doctor_profile_photo/";


// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(AppConfig.LIVE_IMAGE_DOCTOR_API_LINK + link,
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


    private void getLanguage() {
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

    }
}