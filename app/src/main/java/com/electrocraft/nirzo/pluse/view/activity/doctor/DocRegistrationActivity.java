package com.electrocraft.nirzo.pluse.view.activity.doctor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.fragment.DocSignUpEmailFragment;

import timber.log.Timber;

public class DocRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_registration);

        Fragment fragment = new DocSignUpEmailFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.docFrame, fragment);
        ft.commit();

        Timber.d("In doctor Registration ");
    }

}
