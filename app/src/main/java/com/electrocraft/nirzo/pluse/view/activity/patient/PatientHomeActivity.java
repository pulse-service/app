package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.adapter.ViewPagerAdapter;
import com.electrocraft.nirzo.pluse.view.fragment.PtAppointmentFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtDescribeProblemFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtHealthProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtLocationBaseFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtSpecializationFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

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
    private TextView tvNotificationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pt_activity_home);

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


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(PatientHomeActivity.this, DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(this);

        if (navigationView != null) {
            RelativeLayout mParent = (RelativeLayout) navigationView.getHeaderView(0);
            if (mParent != null) {

                TextView userName = mParent.findViewById(R.id.nav_tvPatientNameNavBar);
//                userName.setText(intent.getStringExtra("PTName"));
            }
        }


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
                   /* if (i == 0) {
                        // left
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }*/
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
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
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
