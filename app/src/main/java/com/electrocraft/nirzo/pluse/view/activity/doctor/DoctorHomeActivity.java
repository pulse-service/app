package com.electrocraft.nirzo.pluse.view.activity.doctor;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;


import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AssetUtils;
import com.electrocraft.nirzo.pluse.view.MainActivity;
import com.electrocraft.nirzo.pluse.view.fragment.DocChamberFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.DocTodayAppointFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtHealthProfileFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtProfileFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class DoctorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.doc_drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.doc_nav_view)
    NavigationView navigationView;

   /* @OnClick(R.id.btn_video)
    public void dimOnClick(View view){
        startActivity(new Intent(DoctorHomeActivity.this, MainActivity.class));

    }*/

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
        navigationView.setNavigationItemSelectedListener(this);

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

        switch (item.getItemId()) {

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
            case R.id.nav_manage:
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
}
