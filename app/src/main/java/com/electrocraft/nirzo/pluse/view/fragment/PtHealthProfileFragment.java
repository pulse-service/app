package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nirzo on .
 *
 * @author Faisal Mohammad
 * @since 2/25/2018
 */

public class PtHealthProfileFragment extends Fragment {

    @BindView(R.id.edtWeight)
    EditText edtWeight;
    @BindView(R.id.edtBp)
    EditText edtBloodPressure;
    @BindView(R.id.edtFamilyDiseaseHistory)
    EditText edtFamilyDiseaseHistory;
    @BindView(R.id.edtExistingMedicalReport)
    EditText edtExistingMedicalReport;
    @BindView(R.id.spBloodGroup)
    Spinner spBloodGroup;


    public PtHealthProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_health_profile, container, false);
        ButterKnife.bind(this, view);
        loadBloodGroup();
        return view;
    }

    /**
     * load spinner of Blood Group
     */
    private void loadBloodGroup() {
        List<SpinnerHelper> list = new ArrayList<>();
        SpinnerHelper helper;
        helper = new SpinnerHelper(1, "001", "O+");
        list.add(helper);
        helper = new SpinnerHelper(2, "002", "O-");
        list.add(helper);
        helper = new SpinnerHelper(3, "003", "A+");
        list.add(helper);
        helper = new SpinnerHelper(4, "004", "A-");
        list.add(helper);

        helper = new SpinnerHelper(5, "005", "B+");
        list.add(helper);
        helper = new SpinnerHelper(6, "006", "B-");
        list.add(helper);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spBloodGroup.setAdapter(adapter);
    }
}
