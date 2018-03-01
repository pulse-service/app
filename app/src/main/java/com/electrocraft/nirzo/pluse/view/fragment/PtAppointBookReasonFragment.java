package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.electrocraft.nirzo.pluse.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nirzo on 2/26/2018.
 */

public class PtAppointBookReasonFragment extends Fragment {

    @BindView(R.id.btn_pickTime)
    Button btnPickTime;

    public PtAppointBookReasonFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_appoint_book_reason, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.btn_pickTime)
    public  void onPickTimeClick(View view){
        Fragment fragment= new PtPickUpTimeDateFragment();
        FragmentTransaction ft= getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame,fragment);
        ft.commit();

    }
}
