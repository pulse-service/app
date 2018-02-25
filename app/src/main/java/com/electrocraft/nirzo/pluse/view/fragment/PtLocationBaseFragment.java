package com.electrocraft.nirzo.pluse.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.GeoLayR4Location;
import com.electrocraft.nirzo.pluse.view.adapter.LocationSearchListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzon on 2/20/2018.
 */

public class PtLocationBaseFragment extends Fragment {

    private LocationSearchListAdapter mAdapter;

    @BindView(R.id.recyVLocationSearch)
    RecyclerView rvLocationSearch;

    //Getting the instance of AutoCompleteTextView
    @BindView(R.id.actv_location)
    AutoCompleteTextView actvLocationSearch;

    List<String> autoCtvHelper = new ArrayList<>();


    private List<GeoLayR4Location> mList = new ArrayList<>();

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



        mAdapter = new LocationSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvLocationSearch.setLayoutManager(mLayoutManager);
        rvLocationSearch.setItemAnimator(new DefaultItemAnimator());
        rvLocationSearch.setAdapter(mAdapter);
        prepareData();

        //Creating the instance of ArrayAdapter containing list of autoCtvHelper names
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (getContext(), android.R.layout.select_dialog_item, autoCtvHelper);


        actvLocationSearch.setThreshold(1);                                                         //will start working from first character
        actvLocationSearch.setAdapter(adapter);                                                     //setting the adapter data into the AutoCompleteTextView
        actvLocationSearch.setTextColor(Color.RED);
        return view;
    }

    private void prepareData() {
        GeoLayR4Location loc = new GeoLayR4Location("004", "Mohammadpur");
        mList.add(loc);

        loc = new GeoLayR4Location("004", "Agargou");
        mList.add(loc);

        loc = new GeoLayR4Location("004", "Shamoly");
        mList.add(loc);

        loc = new GeoLayR4Location("004", "Sher-e Bangla");
        mList.add(loc);

        loc = new GeoLayR4Location("004", "Mogbazer");
        mList.add(loc);

        loc = new GeoLayR4Location("004", "Adabor");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "pirer bug");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "badda");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "modho pirer baug");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "china bandam");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "murir bug");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "Time");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "A");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "B");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "C");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "D");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        mList.add(loc);
        loc = new GeoLayR4Location("004", "Mogbazer");
        mList.add(loc);
        for (GeoLayR4Location location : mList) {
            autoCtvHelper.add(location.getLayR4ListName());
        }

        mAdapter.notifyDataSetChanged();
    }
}
