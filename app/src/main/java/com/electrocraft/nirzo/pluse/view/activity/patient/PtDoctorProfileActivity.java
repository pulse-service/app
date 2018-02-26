package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.adapter.ViewPagerAdapter;
import com.electrocraft.nirzo.pluse.view.fragment.PtDescribeProblemFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtDocBlogFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtDocServiceDetailsFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtDoctorDetailsFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtLocationBaseFragment;
import com.electrocraft.nirzo.pluse.view.fragment.PtSpecializationFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PtDoctorProfileActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tvDocName)
    TextView tvDocName;

    private boolean reDirEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_doctor_profile);
        ButterKnife.bind(this);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        reDirEnable = false;


        String dim = getIntent().getStringExtra(Key.DOCTOR_NAME_KEY);
        tvDocName.setText(dim);
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new PtDoctorDetailsFragment(),
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
        if (!reDirEnable)
            finish();
    }

    @OnClick(R.id.btn_appointBook)
    public void onAppointmentBookClick(View view) {
        reDirEnable = true;
        startActivity(new Intent(PtDoctorProfileActivity.this, PtAppointBookActivity.class));
    }
}
