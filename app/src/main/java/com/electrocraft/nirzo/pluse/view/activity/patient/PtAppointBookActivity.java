package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.fragment.PtAppointBookReasonFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

public class PtAppointBookActivity extends AppCompatActivity {

    private String mDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_appoint_book);

        mDoctorId = getIntent().getExtras().getString(Key.KEY_DOCTOR_ID, "");

        Bundle arg = new Bundle();
        arg.putString(Key.KEY_DOCTOR_ID, mDoctorId);

        Fragment faFragment = new PtAppointBookReasonFragment();
        faFragment.setArguments(arg);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, faFragment);
        ft.commit();
    }
}
