package com.electrocraft.nirzo.pluse.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.util.AssetUtils;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by nirzo on 2/28/2018.
 */

public class DocSignUpEmailFragment extends Fragment {

    @BindView(R.id.edt_email)
    EditText edt_pt_email;
    @BindView(R.id.edit_name)
    EditText edt_pt_name;
    @BindView(R.id.edt_phoneNo)
    EditText edt_pt_phone;
    @BindView(R.id.edt_password)
    EditText edt_pt_password;

    @BindView(R.id.spCountryCode)
    Spinner spCountryCode;

    public DocSignUpEmailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_sign_up_email, container, false);
        ButterKnife.bind(this, view);
        loadCountryCodes();
        return view;
    }

    private void loadCountryCodes() {
        List<SpinnerHelper> list = new ArrayList<>();
        try {
            String response = AssetUtils.getJsonAsString("country_code.json", getContext());

            JSONObject object = new JSONObject(response);
            if (!object.isNull("result")) {
                JSONArray array = object.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = (JSONObject) array.get(i);
                    String shortName = object1.getString("shortName");
                    String accessCode = object1.getString("accessCode");

                    SpinnerHelper helper = new SpinnerHelper(i, accessCode, shortName + "(" + accessCode + ")");
                    list.add(helper);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getContext(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spCountryCode.setAdapter(adapter);
    }


    @OnClick(R.id.btnSignUpContinue)
    public void onSignContinueClick(View view) {
        Context context = getActivity();
        if (edt_pt_name.getText().toString().length() == 0) {

            AlertDialogManager.showMissingDialog(getActivity(), "Name Missing");
        } else if (edt_pt_email.getText().toString().length() == 0)
            AlertDialogManager.showMissingDialog(context, "Email Missing");
        else if (edt_pt_phone.getText().toString().length() == 0)
            AlertDialogManager.showMissingDialog(context, "Phone Missing");
        else if (edt_pt_password.getText().toString().length() == 0)
            AlertDialogManager.showErrorDialog(context, "Insert Password");
        else {

            Fragment frag = new DocOTPFragments();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.docFrame,frag);
            ft.commit();

            Timber.d("Go to next  Fragment");
        }

    }

}
