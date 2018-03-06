package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrocraft.nirzo.pluse.R;

import butterknife.ButterKnife;

/**
 * Created by nirzo on 3/5/2018.
 */

public class DocExistingPatientList extends Fragment {
    public DocExistingPatientList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_existing_patient_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
