package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AssetUtils;
import com.electrocraft.nirzo.pluse.model.GeoLayR4Location;
import com.electrocraft.nirzo.pluse.model.deserialization.CurrentDate;
import com.electrocraft.nirzo.pluse.view.adapter.LocationSearchListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on 2/26/2018.
 */

public class PtPickUpTimeDateFragment extends Fragment {

/*    @BindView(R.id.recyVLocationSearch)
    RecyclerView rvLocationSearch;*/
    List<String> autoCtvHelper = new ArrayList<>();

    @BindView(R.id.calendarView)
    CalendarView calendarView;

  /*  private List<GeoLayR4Location> mList = new ArrayList<>();
    private LocationSearchListAdapter mAdapter;*/

    public PtPickUpTimeDateFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_pickup_calander, container, false);
        ButterKnife.bind(this, view);

        CurrentDate jsonResponse;
      /*  int year;
        int month;
*/
        try {
            String response = AssetUtils.getJsonAsString("current_month_n_year.json", getContext());
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            Gson gson = builder.create();

             jsonResponse = gson.fromJson(response, CurrentDate.class);

             Integer.parseInt(jsonResponse.getCurrentMonth());

        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar febFirst = Calendar.getInstance();

//        febFirst.set(2017, 1, 1, 0, 0, 0);

        calendarView.setMinDate(febFirst.getTimeInMillis());

        Calendar febLast = Calendar.getInstance();


        febLast.set(2018, 3, 0, 0, 0, 0);

        calendarView.setMaxDate(febLast.getTimeInMillis());
        calendarView.setDate(febFirst.getTimeInMillis());

  /*      mAdapter = new LocationSearchListAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvLocationSearch.setLayoutManager(mLayoutManager);
        rvLocationSearch.setItemAnimator(new DefaultItemAnimator());
        rvLocationSearch.setAdapter(mAdapter);
        prepareData();*/
        return view;
    }

  /*  private void prepareData() {
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

        for (GeoLayR4Location location : mList) {
            autoCtvHelper.add(location.getLayR4ListName());
        }

        mAdapter.notifyDataSetChanged();
    }*/

}
