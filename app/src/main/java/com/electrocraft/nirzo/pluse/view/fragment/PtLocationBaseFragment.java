package com.electrocraft.nirzo.pluse.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.DoctorSearch;
import com.electrocraft.nirzo.pluse.model.GeoLayR4Location;
import com.electrocraft.nirzo.pluse.view.activity.patient.PtDoctorProfileActivity;
import com.electrocraft.nirzo.pluse.view.adapter.DoctorSearchListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.LocationSearchListAdapter;
import com.electrocraft.nirzo.pluse.view.adapter.RecyclerTouchListener;
import com.electrocraft.nirzo.pluse.view.util.Key;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzon on 2/20/2018.
 */

public class PtLocationBaseFragment extends Fragment {

    private DoctorSearchListAdapter mAdapter;

    @BindView(R.id.recyVLocationSearch)
    RecyclerView rvDocSearch;

    //Getting the instance of AutoCompleteTextView
    @BindView(R.id.actv_location)
    AutoCompleteTextView actvLocationSearch;

    List<String> autoCtvHelper = new ArrayList<>();
    private List<DoctorSearch> mList = new ArrayList<>();

    //private List<GeoLayR4Location> mList = new ArrayList<>();

    public PtLocationBaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_location_base, container, false);
        ButterKnife.bind(this, view);



        mAdapter = new DoctorSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDocSearch.setLayoutManager(mLayoutManager);
        rvDocSearch.setItemAnimator(new DefaultItemAnimator());
        rvDocSearch.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rvDocSearch.setAdapter(mAdapter);
        rvDocSearch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvDocSearch, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DoctorSearch doctorSearch = mList.get(position);
                //  Timber.d("hello Doc");
                Intent intent = new Intent(getActivity(), PtDoctorProfileActivity.class);
                intent.putExtra(Key.DOCTOR_NAME_KEY, doctorSearch.getName());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareData();
        prepareLocationData();

        //Creating the instance of ArrayAdapter containing list of autoCtvHelper names
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.select_dialog_item, autoCtvHelper);


        actvLocationSearch.setThreshold(1);                                                         //will start working from first character
        actvLocationSearch.setAdapter(adapter);                                                     //setting the adapter data into the AutoCompleteTextView
        actvLocationSearch.setTextColor(Color.RED);
        return view;
    }

    private void prepareData() {
        DoctorSearch doctor = new DoctorSearch("Dr. Saleha", "Dhaka medical", true);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. bin", "BAT", false);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. Niloy", "TAB", true);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. Amir", "TAB", false);
        mList.add(doctor);

        doctor = new DoctorSearch("Dr. Jamil", "TAB", true);
        mList.add(doctor);

        mAdapter.notifyDataSetChanged();
    }

    private void prepareLocationData() {
        List<GeoLayR4Location> list = new ArrayList<>();
        GeoLayR4Location loc = new GeoLayR4Location("004", "Mohammadpur");
        list.add(loc);

        loc = new GeoLayR4Location("004", "Agargou");
        list.add(loc);

        loc = new GeoLayR4Location("004", "Shamoly");
        list.add(loc);

        loc = new GeoLayR4Location("004", "Sher-e Bangla");
        list.add(loc);

        loc = new GeoLayR4Location("004", "Mogbazer");
        list.add(loc);

        loc = new GeoLayR4Location("004", "Adabor");
        list.add(loc);
        loc = new GeoLayR4Location("004", "pirer bug");
        list.add(loc);
        loc = new GeoLayR4Location("004", "badda");
        list.add(loc);
        loc = new GeoLayR4Location("004", "modho pirer baug");
        list.add(loc);
        loc = new GeoLayR4Location("004", "china bandam");
        list.add(loc);
        loc = new GeoLayR4Location("004", "murir bug");
        list.add(loc);
        loc = new GeoLayR4Location("004", "Time");
        list.add(loc);
        loc = new GeoLayR4Location("004", "A");
        list.add(loc);
        loc = new GeoLayR4Location("004", "B");
        list.add(loc);
        loc = new GeoLayR4Location("004", "C");
        list.add(loc);
        loc = new GeoLayR4Location("004", "D");
        list.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        list.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        list.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        list.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        list.add(loc);
        for (GeoLayR4Location location : list) {
            autoCtvHelper.add(location.getLayR4ListName());
        }

        mAdapter.notifyDataSetChanged();
    }
}
