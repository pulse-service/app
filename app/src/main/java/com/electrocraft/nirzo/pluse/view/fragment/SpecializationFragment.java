package com.electrocraft.nirzo.pluse.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.adapter.DoctorSearchListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Faisal Mohammad
 * @since 2/20/2018
 */

public class SpecializationFragment extends Fragment {

    @BindView(R.id.sp_specializationCat)
    Spinner spSpecialCat;
    private String[] catName = {"Dentist", "General Physician", "Homeopathy", "Orthopedist"};
    private String[] catCode = {"01", "02", "03", "04"};
    @BindView(R.id.recyclerViewDocSearch)
    RecyclerView rvDocSearch;
    private List<DoctorSearch> mList = new ArrayList<>();


    private DoctorSearchListAdapter mAdapter;


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

        loadCategories();

        mAdapter = new DoctorSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDocSearch.setLayoutManager(mLayoutManager);
        rvDocSearch.setItemAnimator(new DefaultItemAnimator());
        rvDocSearch.setAdapter(mAdapter);
        prepareData();
        return view;

    }

    private void prepareData() {
        DoctorSearch doctor = new DoctorSearch("Dr. Saleha", "Dhaka medical",true);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. bin", "BAT",false);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. Niloy", "TAB",true);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. Amir", "TAB",false);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. Jamil", "TAB",true);
        mList.add(doctor);

        mAdapter.notifyDataSetChanged();
    }

    /**
     * load spinner of Doctor's Specialization
     */
    private void loadCategories() {
        List<SpinnerHelper> list = new ArrayList<>();
        for (int i = 0; i < catName.length; i++) {
            SpinnerHelper helper = new SpinnerHelper(i, catCode[i], catName[i]);
            list.add(helper);
        }
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spSpecialCat.setAdapter(adapter);
    }
}
