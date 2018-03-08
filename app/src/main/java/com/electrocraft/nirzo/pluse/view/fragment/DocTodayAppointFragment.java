package com.electrocraft.nirzo.pluse.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AssetUtils;
import com.electrocraft.nirzo.pluse.model.deserialization.Degree;
import com.electrocraft.nirzo.pluse.model.deserialization.DocAppointments;
import com.electrocraft.nirzo.pluse.model.deserialization.JsonResponse;
import com.electrocraft.nirzo.pluse.model.deserialization.PatientShortInfo;
import com.electrocraft.nirzo.pluse.view.viewhelper.BKViewController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

/**
 * Created by nirzo on
 *
 * @author Faisal Mohammad
 * @since 3/5/2018.
 */

public class DocTodayAppointFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.ll_patient)
    RelativeLayout relativeLayout;

    @BindView(R.id.tv_docName)
    TextView tvDocName;


    @BindView(R.id.tvDocSpecialist)
    TextView tvDocSpecialist;

    @BindView(R.id.tvDocDegree)
    TextView tvDocDegree;

    @BindView(R.id.tvPatientName_1)
    TextView tvPatientName;

    @BindView(R.id.tvTodayLabel)
    TextView tvTodayLabel;

    @BindView(R.id.tvPatientSex)
    TextView tvPatientSex;

    @BindView(R.id.tvPatientProblem)
    TextView tvPatientProblem;

    @BindView(R.id.btnDocS_7_00pm)
    Button btnDocS_7_00pm;

    @BindView(R.id.btnDocS_7_30pm)
    Button btnDocS_7_30pm;

    @BindView(R.id.btnDocS_8_00pm)
    Button btnDocS_8_00pm;

    @BindView(R.id.btnDocS_8_30pm)
    Button btnDocS_8_30pm;

    @BindView(R.id.btnDocS_9_00pm)
    Button btnDocS_9_00pm;

    @BindView(R.id.btnDocS_9_30pm)
    Button btnDocS_9_30pm;

    @BindView(R.id.btnDocS_10_00pm)
    Button btnDocS_10_00pm;

    @BindView(R.id.btnDocS_10_30pm)
    Button btnDocS_10_30pm;

    List<DocAppointments> list = new ArrayList<>();

    @Optional
    @OnClick({R.id.btnDocS_7_00pm, R.id.btnDocS_7_30pm, R.id.btnDocS_8_00pm, R.id.btnDocS_8_30pm})
    public void onClickListenerSee(View view) {
        DocAppointments appointments;
        switch (view.getId()) {
            case R.id.btnDocS_7_00pm:
                appointments = list.get(0);
                setPatientInfoIntoTextView(appointments.getPatients());
                relativeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btnDocS_7_30pm:
                appointments = list.get(1);
                setPatientInfoIntoTextView(appointments.getPatients());
                relativeLayout.setVisibility(View.VISIBLE);
                break;
        }
        tvPatientName.setVisibility(View.VISIBLE);
        tvPatientSex.setVisibility(View.VISIBLE);
        tvPatientProblem.setVisibility(View.VISIBLE);

    }

/*    public static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setEnabled(false);
        }
    };

    public static final ButterKnife.Action<View> ENABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setEnabled(true);
        }
    };*/


    public DocTodayAppointFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_today_appointment, container, false);
        unbinder = ButterKnife.bind(this, view);

        disableAllButton();

        try {
            String response = AssetUtils.getJsonAsString("doc_info.json", getContext());
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            Gson gson = builder.create();

            JsonResponse jsonResponse = gson.fromJson(response, JsonResponse.class);
//            Timber.d(response);

            tvDocName.setText(jsonResponse.getDoctor().getName());

            String docDgree = "";
            for (Degree degree : jsonResponse.getDoctor().getDegree()) {
                docDgree = docDgree + degree.getDegreeName();
                docDgree = docDgree + ", ";
            }

            docDgree = docDgree.substring(0, docDgree.length() - 2);

            tvDocDegree.setText(docDgree);
            tvTodayLabel.setText(jsonResponse.getDoctor().getTodayStr().getToday());
            tvDocSpecialist.setText(jsonResponse.getDoctor().getSpecialist().getSpecName());

            for (DocAppointments appointList : jsonResponse.getDoctor().getAppointments()) {

                switch (appointList.getTime()) {

                    case "07:00pm":
                        if (appointList.isIsBooked()) {
                            enableButton(btnDocS_7_00pm);

                        }
                        break;

                    case "07:30pm":
                        if (appointList.isIsBooked()) {
                            enableButton(btnDocS_7_30pm);

                        }
                        break;

                    case "08:00pm":
                        if (appointList.isIsBooked()) {
                            enableButton(btnDocS_8_00pm);

                        }
                        break;

                    case "08:30pm":
                        if (appointList.isIsBooked()) {
                            enableButton(btnDocS_8_30pm);

                        }
                        break;
                }
                list.add(appointList);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }

    private void setPatientInfoIntoTextView(PatientShortInfo patientInfo) {
        tvPatientName.setText(patientInfo.getName());
        tvPatientSex.setText("(" + patientInfo.getSex() + ")");
        tvPatientProblem.setText(patientInfo.getProb());
    }

    private void resetPatientInfoIntoTextView() {
        tvPatientName.setText("");
        tvPatientSex.setText("sex :");
        tvPatientProblem.setText("");
    }

    private void disableAllButton() {
        ButterKnife.apply(btnDocS_7_00pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_7_30pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_8_00pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_8_30pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_9_00pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_9_30pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_10_00pm, BKViewController.DISABLE);
        ButterKnife.apply(btnDocS_10_30pm, BKViewController.DISABLE);

    }

    private void enableButton(View view) {
        ButterKnife.apply(view, BKViewController.ENABLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
