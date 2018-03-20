package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.fragment.PtAppointBookReasonFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtAppointmentFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtHealthProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtProfileFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PtAppointBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String mDoctorId;
    private String mPatientId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pt_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.pt_nav_view)
    NavigationView navigationView;
    private String mDocExpertise;
    private String mDocAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_appoint_book);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {


            mDoctorId = getIntent().getExtras().getString(Key.KEY_DOCTOR_ID, "");
            mDocExpertise = getIntent().getExtras().getString(Key.KEY_DOCTOR_EXPERTISE, "");
            mDocAmount = getIntent().getExtras().getString(Key.KEY_DOCTOR_AMOUNT, "");
//            intent.putExtra(Key.KEY_DOCTOR_EXPERTISE, mDocExpertise);
        }

        showFragment();
        mPatientId = AppSharePreference.getPatientID(this);

        // toolbar
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {                                    // safety block
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        if (navigationView != null) {
            RelativeLayout mParent = (RelativeLayout) navigationView.getHeaderView(0);
            if (mParent != null) {

                TextView userName = mParent.findViewById(R.id.nav_tvPatientNameNavBar);
//                userName.setText(intent.getStringExtra("PTName"));
            }
        }
    }

    private void showFragment() {
        Bundle arg = new Bundle();
        arg.putString(Key.KEY_DOCTOR_ID, mDoctorId);
        arg.putString(Key.KEY_DOCTOR_EXPERTISE, mDocExpertise);
        arg.putString(Key.KEY_DOCTOR_AMOUNT, mDocAmount);


        Fragment faFragment = new PtAppointBookReasonFragment();
        faFragment.setArguments(arg);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, faFragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                startActivity(new Intent(this, PatientHomeActivity.class));
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

            case R.id.nav_logout:

                Intent intent = new Intent(Intent.ACTION_MAIN);
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
