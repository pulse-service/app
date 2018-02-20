package com.electrocraft.nirzo.pluse.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.electrocraft.nirzo.pluse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzon on 2/20/2018.
 *
 * @author Faisal Mohammad
 */

public class SpecializationFragment extends Fragment {

    @BindView(R.id.sp_specializationCat)
    Spinner spSpecialCat;
    private String[] categories = {"Dentist", "General Physician", "Homeopathy", "Orthopedist"};

    public SpecializationFragment() {
        // required  empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_specialization, container, false);
        ButterKnife.bind(this, view);


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(),
                R.layout.rsc_spinner_text, categories);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spSpecialCat.setAdapter(adapter);
        return view;

    }
}
