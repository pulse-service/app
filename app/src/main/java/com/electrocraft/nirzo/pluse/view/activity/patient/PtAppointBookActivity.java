package com.electrocraft.nirzo.pluse.view.activity.patient;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.fragment.PtAppointBookReasonFragment;

public class PtAppointBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_appoint_book);

        Fragment faFragment=new PtAppointBookReasonFragment();

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,faFragment);
        ft.commit();
    }
}
