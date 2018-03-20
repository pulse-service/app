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


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.fragment.DocAppointmentFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocChamberFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocTodayAppointFragment;

import org.json.JSONArray;
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

    @BindView(R.id.iv_doc_cover_pic)
    ImageView ivDocCoverPic;

    private ProgressDialog pDialog;


    String token = "";
    private String mDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(DoctorHomeActivity.this, DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);


        mDoctorId = AppSharePreference.getDoctorID(this);
//        timeConsume();

        Log.d("PLTO", " mDoctorId :" + mDoctorId);
//        getPatientAppointment(mDoctorId);
//        getDoctorImageRequest();

       /* Test Json resouce reader
        try {
            Timber.d(AssetUtils.getJsonAsString("files.json",this));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
//        ButterKnife.apply(ivDocCoverPic, BKViewController.ENABLE);
        ivDocCoverPic.setVisibility(View.GONE);
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

                Intent intent = new Intent(Intent.ACTION_MAIN);
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


    private  void getPatientAppointment(final String doctorId) {
        String patient_login_tag = "doctor_s_appointment";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "searchCallRequestForDoctor?receiverid=" + doctorId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Log.d("PLTO", response);

                        /**
                         * {
                         "status": "success",
                         "data": [
                         {
                         "receiverid": "DR000000001",
                         "senderId": "ECL-00000001",
                         "receiverType": "Doctor"
                         }
                         ],
                         "msg": "Call Request List"
                         }
                         */

                        String receiverid = "";
                        String senderId = "";
                        String receiverType = "";


                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getString("status").equals("success")) {
                                if (!object.isNull("data")) {
                                    JSONArray array = object.getJSONArray("data");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object1 = array.getJSONObject(i);

                                        receiverid = object1.getString("receiverid");
                                        senderId = object1.getString("senderId");
                                        receiverType = object1.getString("receiverType");

                                        Intent intent = new Intent();
                                        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
                                        sendBroadcast(intent);

                                /*        IntentFilter filter = new IntentFilter(ACTION);
                                        DoctorHomeActivity.registerReceiver(mReceivedSMSReceiver, filter);*/

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("doctorId", doctorId);


                return params;
            }
        };

        AppController.getInstance().

                addToRequestQueue(stringRequest, patient_login_tag);

    }


   /* private final BroadcastReceiver mReceivedSMSReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION.equals(action))
            {
                //your SMS processing code
                displayAlert();
            }
        }
    };*/


  /*  public void showRiningMessage(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog ad = builder.create();
//                .create();


   *//*     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String selectedDate = sdf.format(new Date(calendarView.getDate()));
        ad.setCancelable(false);*//*
        ad.setTitle("Ringing");
        ad.setMessage("You have Call");
        ad.setButton("Accept ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


            }
        });
        ad.setButton("Reject ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        ad.show();
    }*/

    private void getDoctorImageRequest() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        String url = "http://192.168.1.7/elc_api/public/image/img.jpg";


// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
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


    private void timeConsume() {

/*
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.API_LINK + "auth/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                AppController.getInstance().getRequestQueue().getCache().clear();
                try {
                    JSONObject jos = new JSONObject(response);
                    if (!jos.isNull("token")) {
                        token = jos.getString("token");
                        if (token.length() > 6)
//                            heatStoke(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("more", response);
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
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

        AppController.getInstance().addToRequestQueue(stringRequest, "token");*/
    }


  /*  private void heatStoke(final String token1) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.7/elc_api/public/api/user_list";

//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                AppController.getInstance().getRequestQueue().getCache().clear();
                Log.d("DAM", response);
                pDialog.hide();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token1);
                return params;
            }
        };


// Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }*/
}
