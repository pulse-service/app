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
 *
 * @author Faisal
 * @since 3/17/2018
 */

public class PtAppointmentFragment extends Fragment {


    public PtAppointmentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_s_appointment_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
