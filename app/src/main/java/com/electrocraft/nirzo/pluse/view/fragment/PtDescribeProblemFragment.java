package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.electrocraft.nirzo.pluse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzon on 2/20/2018.
 */

public class PtDescribeProblemFragment extends Fragment {

    @BindView(R.id.edtSymptomDes)
    EditText edtSymptomDetails ;
    @BindView(R.id.btn_symptom_submit)
    Button btnSubmit;

    public PtDescribeProblemFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_describe_problem, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
