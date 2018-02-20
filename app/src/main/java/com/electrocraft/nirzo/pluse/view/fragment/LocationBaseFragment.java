package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.electrocraft.nirzo.pluse.R;

/**
 * Created by nirzon on 2/20/2018.
 */

public class LocationBaseFragment extends Fragment {

    public LocationBaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_location_base,container,false);
    }
}
