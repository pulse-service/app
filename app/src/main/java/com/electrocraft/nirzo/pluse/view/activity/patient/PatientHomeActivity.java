package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.activity.LoginAsActivity;
import com.electrocraft.nirzo.pluse.view.adapter.ViewPagerAdapter;
import com.electrocraft.nirzo.pluse.view.fragment.PatientPrescriptionFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtAppointmentFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtHealthProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtLocationBaseFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtSpecializationFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pt_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.pt_nav_view)
    NavigationView navigationView;

    String mPatientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pt_activity_home);
        queue = Volley.newRequestQueue(this);

        ButterKnife.bind(this);

        /*
        * work in late
         */
        Intent intent = getIntent();          // work in late

        mPatientId = AppSharePreference.getPatientID(this);

        // toolbar
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {                                    // safety block
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        wrapTabIndicatorToTitle(tabLayout, 20, 20);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(PatientHomeActivity.this, DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(this);

        /*if (navigationView != null) {
            RelativeLayout mParent = (RelativeLayout) navigationView.getHeaderView(0);
            if (mParent != null) {

                //TextView userName = mParent.findViewById(R.id.nav_tvPatientNameNavBar);
//                userName.setText(intent.getStringExtra("PTName"));
            }
        }*/

        if (getIntent().getBooleanExtra("redirfrom", false)) {
            Bundle arg = new Bundle();
            arg.putString(Key.KEY_PATIENT_ID, mPatientId);
            Fragment fragment = new PtAppointmentFragment();
            fragment.setArguments(arg);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        callPatientInfo();

    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        settingMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        settingMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        settingMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void settingMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new PtSpecializationFragment(),
                getResources().getString(R.string.specialization));

        adapter.addFragment(new PtLocationBaseFragment(),
                getResources().getString(R.string.location_base));

 /*       adapter.addFragment(new PtDescribeProblemFragment(),
                getResources().getString(R.string.describe_problem));*/

        viewPager.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Are you sure you want to exit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                backButton();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        }
    }

    public void backButton() {
        super.onBackPressed();

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        Bundle arg = new Bundle();
        arg.putString(Key.KEY_PATIENT_ID, mPatientId);

        switch (item.getItemId()) {


            case R.id.nav_home:

                //Todo need to migrate the functionalities of this class to a fragment and load it from this activity. This activity will contain the navigation system

                Intent intenT = new Intent(PatientHomeActivity.this, PatientHomeActivity.class);
                intenT.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intenT);
                break;
            case R.id.nav_appointment:
                fragment = new PtAppointmentFragment();
                title = "";
                break;
            case R.id.nav_profile:
                fragment = new PtProfileFragment();
                title = "";
                break;

            case R.id.nav_health:
                fragment = new PtHealthProfileFragment();
                title = "";
                break;

            case R.id.nav_prescription:
                fragment = new PatientPrescriptionFragment();
                title = "";
                break;
            case R.id.nav_logout:
                AppSharePreference.saveDoctorID(this, "");
                AppSharePreference.savePatientID(this, "");
                Intent intent = new Intent(PatientHomeActivity.this, LoginAsActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }

        // this if block the null point Exception if Fragment is null
        if (fragment != null) {

            //set Bundle
            fragment.setArguments(arg);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.add(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    RequestQueue queue;

    private void callPatientInfo() {
        String url = "http://180.148.210.139:8081/pulse_api/api/patientregistration/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + AppSharePreference.getPatientID(this),
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
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("PRI_PTName");
                                pat_name.setText(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("PRI_Phone");
                                pat_number.setText(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("PRI_Email");
                                pat_email.setText(jsonObjectInside);


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



}
