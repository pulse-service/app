package com.electrocraft.nirzo.pluse.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.util.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by nirzo on 2/26/2018.
 */

public class PtAppointBookReasonFragment extends Fragment {

/*    @BindView(R.id.btn_pickTime)
    Button btnPickTime;*/

    @BindView(R.id.edtPatientDoctorAppointmentReason)
    EditText edtShortDescribtion;




    @BindView(R.id.cv_4)
    CardView cv_4;


    private String mDoctorId;
    private String mDocExpertise;
    private String mDocAmount;

    public PtAppointBookReasonFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getView() != null)
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_appoint_book_reason, container, false);
        ButterKnife.bind(this, view);

        Bundle agr = getArguments();
        if (agr != null) {

            mDoctorId = agr.getString(Key.KEY_DOCTOR_ID, "");
            mDocExpertise = agr.getString(Key.KEY_DOCTOR_EXPERTISE, "");
            mDocAmount = agr.getString(Key.KEY_DOCTOR_AMOUNT, "");


        }

        // hide the soft input
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return view;
    }

    @Optional
    @OnClick({R.id.cv2, R.id.btn_bookAppointment})
    public void onPickTimeClick() {
        Fragment fragment = new PtPickUpTimeDateFragment();
        String shortDescribtion = edtShortDescribtion.getText().toString();

        if (mDoctorId != null && mDoctorId.length() > 0) {
            Bundle arg = new Bundle();
            arg.putString(Key.KEY_DOCTOR_ID, mDoctorId);
            arg.putString(Key.KEY_PATIENT_PROBLEM_SHORT_DES, shortDescribtion);
            arg.putString(Key.KEY_DOCTOR_EXPERTISE, mDocExpertise);
            arg.putString(Key.KEY_DOCTOR_AMOUNT, mDocAmount);
            fragment.setArguments(arg);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }
}
