package com.electrocraft.nirzo.pluse.view.activity.doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.activity.LoginAsActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.fragment.DocAppointmentFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocChamberFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocTodayAppointFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DoctorHomeFragment;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final String ACTION = "com.tutorialspoint.CUSTOM_INTENT";
    private static final String TAG = "DoctorHomeActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.doc_drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.doc_nav_view)
    NavigationView navigationView;


    private ProgressDialog pDialog;
    RequestQueue queue;

    String token = "";
    private String mDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity_home);
        ButterKnife.bind(this);
        queue = Volley.newRequestQueue(this);
        callDoctorInfo();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(DoctorHomeActivity.this, DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);


        mDoctorId = AppSharePreference.getDoctorID(this);

//        Fragment home = DoctorHomeFragment.newInstance("", "");
        Fragment home = new DocAppointmentFragment();


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.doc_content_frame, home);
        ft.commit();
//        timeConsume();

        Log.d("PLTO", " mDoctorId :" + mDoctorId);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
//        ButterKnife.apply(ivDocCoverPic, BKViewController.ENABLE);

        switch (item.getItemId()) {

            case R.id.nav_home:
                startActivity(new Intent(this, DoctorHomeActivity.class));
                break;

            case R.id.nav_doc_appointment:
                fragment = new DocAppointmentFragment();
                title = "";
                break;
            case R.id.nav_doc_profile:
                fragment = new DocProfileFragment();
                title = "Profile";
                break;

            case R.id.nav_doc_chamber:
                fragment = new DocChamberFragment();
                title = "Health";
                break;

            case R.id.nav_doc_today:
                fragment = new DocTodayAppointFragment();
                title = "Today's";
                break;

            case R.id.nav_logout:
                LoginManager.getInstance().logOut();
                AppSharePreference.saveDoctorID(this, "");
                AppSharePreference.savePatientID(this, "");
                Intent intent = new Intent(DoctorHomeActivity.this, LoginAsActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }

        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.doc_content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void callDoctorInfo() {
        String url = "http://180.148.210.139:8081/pulse_api/api/getdoctorProfileView/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + AppSharePreference.getDoctorID(this),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            View hView = navigationView.getHeaderView(0);

                            TextView pat_name = hView.findViewById(R.id.nav_tvPatientNameNavBar);

                            TextView pat_number = hView.findViewById(R.id.tvPatNumber);

                            TextView pat_email = hView.findViewById(R.id.tvpatemail);

                            ImageView pat_img = hView.findViewById(R.id.imageView);
                            String jsonObjectInside;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_DrName");
                                pat_name.setText(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_Phone");
                                pat_number.setText(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_Email");
                                pat_email.setText(jsonObjectInside);


                                jsonObjectInside = jsonArray.getJSONObject(i).getString("Photo");
                                getDoctorImageRequest(jsonObjectInside, pat_img);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getDoctorImageRequest(String imageLink, final ImageView imageView) {


// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(AppConfig.LIVE_IMAGE_DOCTOR_API_LINK + imageLink,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
//                        pDialog.hide();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.ic_doctor);
//                        pDialog.hide();
                    }
                });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request);
    }

}
