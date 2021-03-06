package com.electrocraft.nirzo.pluse.view.activity.doctor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.AppSharePreference;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorPrescriptionActivity extends AppCompatActivity {
    ArrayList<SymptomModel> symptomModelArrayList;
    ArrayList<TestModel> symptomwiseTestArralist;
    ArrayList<TestModel> symptomwiseTestArralistEdit;
    ArrayList<GenericModel> genericArralist;
    ArrayList<DrugModel> genericWiseDrugArralist;
    ArrayList<DrugModel> genericWiseDrugArralistEdit;
    ArrayList<StrengthModel> strengthArraylist;
    ArrayList<StrengthModel> strengthArraylistEdit;
    ArrayList<String> daysArraylist;

    ArrayList<LabTestModel> labTestModelArrayList;
    ArrayList<DrugRVModel> drugRVModelArrayList;

    RequestQueue queue;
    @BindView(R.id.doc_pres_symptom_spinner)
    Spinner symptomSpinner;
    @BindView(R.id.doc_pres_symptom_ecg)
    Spinner testSpinner;
    @BindView(R.id.doc_pres_RX_symptom_spinner)
    Spinner genericSpinner;
    @BindView(R.id.doc_pres_RX_drugs_spinner)
    Spinner drugSpinner;
    @BindView(R.id.doc_pres_RX_strength_spinner)
    Spinner strengthSpinner;
    @BindView(R.id.doc_pres_RX_days_spinner)
    Spinner daysSpinner;
    @BindView(R.id.rv_Drugs)
    RecyclerView rxRV;
    @BindView(R.id.doc_press_et_diagnosis)
    EditText diagnosis;
    @BindView(R.id.doc_press_et_findings)
    EditText findings;
    @BindView(R.id.doc_press_et_sig)
    EditText signature;

    @BindView(R.id.rv_labtest)
    RecyclerView labtestRV;

    @BindView(R.id.doc_press_DocName)
    TextView doc_press_DocName;

    @BindView(R.id.doc_pres_doc_number)
    TextView doc_pres_doc_number;

    @BindView(R.id.doc_pres_doc_email)
    TextView doc_pres_doc_email;


    @BindView(R.id.doc_pres_doc_id)
    TextView doc_pres_doc_id;


    @BindView(R.id.doc_pres_iv_DocImage)
    CircleImageView doc_pres_iv_DocImage;

    ArrayAdapter<SymptomModel> adapterSymptom;
    ArrayAdapter<TestModel> adapterTest;
    ArrayAdapter<TestModel> adapterTestEdit;
    ArrayAdapter<GenericModel> adapterGeneric;
    ArrayAdapter<DrugModel> adapterDrug;
    ArrayAdapter<DrugModel> adapterDrugEdit;
    ArrayAdapter<StrengthModel> adapterStrength;
    ArrayAdapter<StrengthModel> adapterStrengthEdit;
    ArrayAdapter<String> adapterDays;

    LabTestAdapter labTestAdapter;
    DrugsAdapter drugsAdapter;

    private String savecode;
    private ProgressDialog pDialog;
    private String mConsultationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_prescription);
        ButterKnife.bind(this);
        queue = Volley.newRequestQueue(this);
// todo: ge id

        mConsultationId = getIntent().getStringExtra("consult_id");

        callDoctorInfo();
        symptomModelArrayList = new ArrayList<>();
        symptomwiseTestArralist = new ArrayList<>();
        symptomwiseTestArralistEdit = new ArrayList<>();
        genericArralist = new ArrayList<>();
        genericWiseDrugArralist = new ArrayList<>();
        genericWiseDrugArralistEdit = new ArrayList<>();
        strengthArraylist = new ArrayList<>();
        strengthArraylistEdit = new ArrayList<>();
        daysArraylist = new ArrayList<>();
        labTestModelArrayList = new ArrayList<>();
        drugRVModelArrayList = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            daysArraylist.add(i + "");
        }

        SymptomModel m = new SymptomModel();
        m.setName("Loading");
        symptomModelArrayList.add(m);

        TestModel testModel = new TestModel();
        testModel.setTestName("Loading");
        symptomwiseTestArralist.add(testModel);

        GenericModel genericModel = new GenericModel();
        genericModel.setGenericName("Loading");
        genericArralist.add(genericModel);

        DrugModel drugModel = new DrugModel();
        drugModel.setDrugName("Loading");
        genericWiseDrugArralist.add(drugModel);

        StrengthModel strengthModel = new StrengthModel();
        strengthModel.setStrengthName("Loading");
        strengthArraylist.add(strengthModel);

        adapterSymptom = new ArrayAdapter<SymptomModel>(getApplicationContext(), android.R.layout.simple_spinner_item, symptomModelArrayList);
        adapterSymptom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        symptomSpinner.setAdapter(adapterSymptom);

        adapterTest = new ArrayAdapter<TestModel>(getApplicationContext(), android.R.layout.simple_spinner_item, symptomwiseTestArralist);
        adapterTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testSpinner.setAdapter(adapterTest);

        adapterTestEdit = new ArrayAdapter<TestModel>(getApplicationContext(), android.R.layout.simple_spinner_item, symptomwiseTestArralistEdit);
        adapterTestEdit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterGeneric = new ArrayAdapter<GenericModel>(getApplicationContext(), android.R.layout.simple_spinner_item, genericArralist);
        adapterGeneric.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genericSpinner.setAdapter(adapterGeneric);

        adapterDrug = new ArrayAdapter<DrugModel>(getApplicationContext(), android.R.layout.simple_spinner_item, genericWiseDrugArralist);
        adapterDrug.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drugSpinner.setAdapter(adapterDrug);

        adapterDrugEdit = new ArrayAdapter<DrugModel>(getApplicationContext(), android.R.layout.simple_spinner_item, genericWiseDrugArralistEdit);
        adapterDrugEdit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterStrength = new ArrayAdapter<StrengthModel>(getApplicationContext(), android.R.layout.simple_spinner_item, strengthArraylist);
        adapterStrength.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        strengthSpinner.setAdapter(adapterStrength);

        adapterStrengthEdit = new ArrayAdapter<StrengthModel>(getApplicationContext(), android.R.layout.simple_spinner_item, strengthArraylistEdit);
        adapterStrengthEdit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterDays = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, daysArraylist);
        adapterDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(adapterDays);

        symptomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                symptomwiseTestArralist.clear();
                TestModel testModel = new TestModel();
                testModel.setTestName("Loading");
                symptomwiseTestArralist.add(testModel);
                adapterTest.notifyDataSetChanged();
                callSymptomWiseTest(i, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        genericSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Testings", "generic item: " + genericArralist.get(i).getInfoCode());
                genericWiseDrugArralist.clear();
                DrugModel drugModel = new DrugModel();
                drugModel.setDrugName("Loading");
                genericWiseDrugArralist.add(drugModel);
                adapterDrug.notifyDataSetChanged();
                savecode = genericArralist.get(i).getInfoCode();
                callGenWiseDrug(i, genericArralist.get(i).getInfoCode(), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Testings", "generic onNothingSelected: ");
            }
        });
        drugSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strengthArraylist.clear();
                StrengthModel strengthModel = new StrengthModel();
                strengthModel.setStrengthName("Loading");
                strengthArraylist.add(strengthModel);
                adapterStrength.notifyDataSetChanged();
                Log.d("Testings", "drug spinner changed: ");
                callDrugWiseStrength(genericWiseDrugArralist.get(i).getDrugCode(), false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        labTestAdapter = new LabTestAdapter(labTestModelArrayList, new LabTestAdapter.EditClickListener() {
            @Override
            public void OnClick(int position) {
                Log.d("sss", "Labtest: " + position);
                labtestEditRow(position);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        labtestRV.setLayoutManager(mLayoutManager);
        labtestRV.setItemAnimator(new DefaultItemAnimator());
        labtestRV.setAdapter(labTestAdapter);

        drugsAdapter = new DrugsAdapter(drugRVModelArrayList, new DrugsAdapter.EditClickListener() {
            @Override
            public void setEditClickListener(int position) {
                Log.d("sss", "RX: " + position);
                rxEditRow(position);
            }

        });
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        rxRV.setLayoutManager(mLayoutManager2);
        rxRV.setItemAnimator(new DefaultItemAnimator());
        rxRV.setAdapter(drugsAdapter);

        callSymptomList();
        callGenericList();

    }

    private void labtestEditRow(final int rvPosition) {
        LayoutInflater li = LayoutInflater.from(this);

        View promptsView = li.inflate(R.layout.labtest_editbox, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        // set dialog message

        alertDialogBuilder.setTitle("Edit entry");
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Spinner mSpinner = (Spinner) promptsView
                .findViewById(R.id.doc_pres_symptom_spinner_edit);
        final Spinner mSpinner2 = (Spinner) promptsView
                .findViewById(R.id.doc_pres_symptom_ecg_edit);
        final Button mButtonOk = (Button) promptsView
                .findViewById(R.id.doc_pres_lab_ok_button_edit);

        final Button mButtonCancel = (Button) promptsView
                .findViewById(R.id.doc_pres_lab_cancel_button_edit);

        final Button mButtonDelete = (Button) promptsView
                .findViewById(R.id.doc_pres_lab_delete_button_edit);

        mSpinner.setAdapter(adapterSymptom);
        mSpinner2.setAdapter(adapterTestEdit);
// reference UI elements from my_dialog_layout in similar fashion
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                symptomwiseTestArralistEdit.clear();
                TestModel testModel = new TestModel();
                testModel.setTestName("Loading");
                symptomwiseTestArralistEdit.add(testModel);
                adapterTestEdit.notifyDataSetChanged();
                callSymptomWiseTest(i, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sss", "ok with value: " + symptomModelArrayList.get(mSpinner.getSelectedItemPosition()).getName() + symptomwiseTestArralistEdit.get(mSpinner2.getSelectedItemPosition()).getTestName());
                LabTestModel labTestModel = new LabTestModel();
                labTestModel.setSymptom(symptomModelArrayList.get(mSpinner.getSelectedItemPosition()).getName());
                labTestModel.setSymptomCode(symptomModelArrayList.get(mSpinner.getSelectedItemPosition()).getCode());
                labTestModel.setTest(symptomwiseTestArralist.get(mSpinner2.getSelectedItemPosition()).getTestName());
                labTestModel.setTestCode(symptomwiseTestArralist.get(mSpinner2.getSelectedItemPosition()).getTestCode());
                labTestModelArrayList.remove(rvPosition);
                labTestModelArrayList.add(rvPosition, labTestModel);
                labTestAdapter.notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                labTestModelArrayList.remove(rvPosition);
                labTestAdapter.notifyDataSetChanged();
                alertDialog.dismiss();

            }
        });

// show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void rxEditRow(final int rvPosition) {
        LayoutInflater li = LayoutInflater.from(this);

        View promptsView = li.inflate(R.layout.rx_editbox, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        // set dialog message

        alertDialogBuilder.setTitle("Edit entry");
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Spinner mSpinner = (Spinner) promptsView
                .findViewById(R.id.doc_pres_RX_symptom_spinner_edit);
        final Spinner mSpinner2 = (Spinner) promptsView
                .findViewById(R.id.doc_pres_RX_drugs_spinner_edit);
        final Spinner mSpinner3 = (Spinner) promptsView
                .findViewById(R.id.doc_pres_RX_strength_spinner_edit);
        final Spinner mSpinner4 = (Spinner) promptsView
                .findViewById(R.id.doc_pres_RX_days_spinner_edit);
        final EditText signature = (EditText) promptsView.findViewById(R.id.doc_pres_sig_edit);
        final Button mButtonOk = (Button) promptsView
                .findViewById(R.id.doc_pres_rx_ok_button_edit);

        final Button mButtonCancel = (Button) promptsView
                .findViewById(R.id.doc_pres_rx_cancel_button_edit);

        final Button mButtonDelete = (Button) promptsView
                .findViewById(R.id.doc_pres_rx_delete_button_edit);

        mSpinner.setAdapter(adapterGeneric);
        mSpinner2.setAdapter(adapterDrugEdit);
        mSpinner3.setAdapter(adapterStrengthEdit);
        mSpinner4.setAdapter(adapterDays);
        // reference UI elements from my_dialog_layout in similar fashion
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genericWiseDrugArralistEdit.clear();
                DrugModel drugModel = new DrugModel();
                drugModel.setDrugName("Loading");
                genericWiseDrugArralistEdit.add(drugModel);
                adapterDrugEdit.notifyDataSetChanged();
                savecode = genericArralist.get(i).getInfoCode();
                callGenWiseDrug(i, genericArralist.get(i).getInfoCode(), true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strengthArraylistEdit.clear();
                StrengthModel strengthModel = new StrengthModel();
                strengthModel.setStrengthName("Loading");
                Log.d("sss", "loading clear: ");
                strengthArraylistEdit.add(strengthModel);
                adapterStrengthEdit.notifyDataSetChanged();
                callDrugWiseStrength(genericWiseDrugArralistEdit.get(i).getDrugCode(), true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrugRVModel drugRVModel = new DrugRVModel();
                drugRVModel.setDrugs(genericWiseDrugArralistEdit.get(mSpinner2.getSelectedItemPosition()).getDrugName());
                drugRVModel.setDrugsCode(genericWiseDrugArralistEdit.get(mSpinner2.getSelectedItemPosition()).getDrugCode());
                drugRVModel.setStrength(strengthArraylistEdit.get(mSpinner3.getSelectedItemPosition()).getStrengthName());
                drugRVModel.setStrengthCode(strengthArraylistEdit.get(mSpinner3.getSelectedItemPosition()).getStrengthCode());
                drugRVModel.setDays(daysArraylist.get(mSpinner4.getSelectedItemPosition()));
                drugRVModel.setTimes(signature.getText().toString());
                drugRVModelArrayList.remove(rvPosition);
                drugRVModelArrayList.add(rvPosition, drugRVModel);
                drugsAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drugRVModelArrayList.remove(rvPosition);
                drugsAdapter.notifyDataSetChanged();
                alertDialog.dismiss();

            }
        });
        // show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void callSymptomList() {

// Instantiate the RequestQueue.

        String url = "http://180.148.210.139:8081/pulse_api/api/getSymptomList";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            symptomModelArrayList.clear();
                            SymptomModel sm = new SymptomModel();
                            sm.setName("Select");
                            symptomModelArrayList.add(sm);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SymptomModel m = new SymptomModel();
                                String jsonObjectInside = jsonArray.getJSONObject(i).getString("SymptomCode");
                                m.setCode(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("SymptomName");
                                m.setName(jsonObjectInside);
                                symptomModelArrayList.add(m);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            Log.d("wasiun", "onResponse: " + symptomModelArrayList.size());
                            adapterSymptom.notifyDataSetChanged();
                            //set to spinner todo
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void callSymptomWiseTest(int i, final boolean fromEdit) {
        String url = "http://180.148.210.139:8081/pulse_api/api/getSymptomWiseLabTestList/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + symptomModelArrayList.get(i).getCode(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (fromEdit) {
                                symptomwiseTestArralistEdit.clear();
                                TestModel tm = new TestModel();
                                tm.setTestName("Select");
                                symptomwiseTestArralistEdit.add(tm);
                            } else {
                                symptomwiseTestArralist.clear();
                                TestModel tm = new TestModel();
                                tm.setTestName("Select");
                                symptomwiseTestArralist.add(tm);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                TestModel m = new TestModel();
                                String jsonObjectInside = jsonArray.getJSONObject(i).getString("TestName");
                                m.setTestName(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("LabTestCode");
                                m.setTestCode(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("SymptomCode");
                                m.setSymptomCode(jsonObjectInside);
                                if (fromEdit) {
                                    symptomwiseTestArralistEdit.add(m);
                                } else {
                                    symptomwiseTestArralist.add(m);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            Log.d("wasiun", "onResponse: " + symptomModelArrayList.size());
                            if (fromEdit) {
                                adapterTestEdit.notifyDataSetChanged();
                            } else {
                                adapterTest.notifyDataSetChanged();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void callGenericList() {

// Instantiate the RequestQueue.

        String url = "http://180.148.210.139:8081/pulse_api/api/getGenericList";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            genericArralist.clear();
                            GenericModel gm = new GenericModel();
                            gm.setGenericName("Select");
                            genericArralist.add(gm);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                GenericModel m = new GenericModel();
                                String jsonObjectInside = jsonArray.getJSONObject(i).getString("InfoCode");
                                m.setInfoCode(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("GenericName");
                                m.setGenericName(jsonObjectInside);
                                genericArralist.add(m);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            adapterGeneric.notifyDataSetChanged();
                            //set to spinner todo
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void callGenWiseDrug(int i, String infoCode, final boolean fromEdit) {

        Log.d("sss", "callGenWiseDrug: " + savecode);

        String url = "http://180.148.210.139:8081/pulse_api/api/getGenericWiseDrugList/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + infoCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            DrugModel dm = new DrugModel();
                            dm.setDrugName("Select");
                            if (fromEdit) {
                                genericWiseDrugArralistEdit.clear();
                                genericWiseDrugArralistEdit.add(dm);
                            } else {
                                genericWiseDrugArralist.clear();
                                genericWiseDrugArralist.add(dm);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                DrugModel m = new DrugModel();
                                String jsonObjectInside = jsonArray.getJSONObject(i).getString("DrugName");
                                m.setDrugName(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DrugCode");
                                m.setDrugCode(jsonObjectInside);
                                if (fromEdit) {
                                    genericWiseDrugArralistEdit.add(m);
                                } else {
                                    genericWiseDrugArralist.add(m);

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (fromEdit) {
                                adapterDrugEdit.notifyDataSetChanged();
                            } else {
                                adapterDrug.notifyDataSetChanged();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void callDrugWiseStrength(String drugCode, final boolean fromEdit) {
        String url = "http://180.148.210.139:8081/pulse_api/api/getDrugWiseStrengthList/";

        Log.d("sss", "callDrugWiseStrength: " + url + savecode + "/" + drugCode);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + savecode + "/" + drugCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            StrengthModel sm = new StrengthModel();
                            sm.setStrengthName("Select");
                            if (fromEdit) {
                                strengthArraylistEdit.clear();
                                strengthArraylistEdit.add(sm);
                            } else {
                                strengthArraylist.clear();
                                strengthArraylist.add(sm);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                StrengthModel m = new StrengthModel();
                                String jsonObjectInside = jsonArray.getJSONObject(i).getString("SI_StrenthDescription");
                                m.setStrengthName(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("SI_StrengthInfoCode");
                                m.setStrengthCode(jsonObjectInside);
                                Log.d("sss", "onResponse: " + m.getStrengthName());
                                if (fromEdit) {
                                    strengthArraylistEdit.add(m);
                                } else {
                                    strengthArraylist.add(m);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (fromEdit) {
                                adapterStrengthEdit.notifyDataSetChanged();
                            } else {
                                adapterStrength.notifyDataSetChanged();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void labTestAdd(View view) {
        if (symptomSpinner.getSelectedItemPosition() == 0 || testSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the fields.", Toast.LENGTH_SHORT).show();
        } else {
            LabTestModel labTestModel = new LabTestModel();
            labTestModel.setSymptom(symptomModelArrayList.get(symptomSpinner.getSelectedItemPosition()).getName());
            labTestModel.setSymptomCode(symptomModelArrayList.get(symptomSpinner.getSelectedItemPosition()).getCode());
            labTestModel.setTest(symptomwiseTestArralist.get(testSpinner.getSelectedItemPosition()).getTestName());
            labTestModel.setTestCode(symptomwiseTestArralist.get(testSpinner.getSelectedItemPosition()).getTestCode());
            labTestModelArrayList.add(labTestModel);
            labTestAdapter.notifyDataSetChanged();
            symptomSpinner.setSelection(0);
            testSpinner.setSelection(0);
        }
    }

    public void drugAdd(View view) {
        if (drugSpinner.getSelectedItemPosition() == 0 || strengthSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select the fields.", Toast.LENGTH_SHORT).show();
        } else {
            DrugRVModel drugRVModel = new DrugRVModel();
            drugRVModel.setDrugs(genericWiseDrugArralist.get(drugSpinner.getSelectedItemPosition()).getDrugName());
            drugRVModel.setDrugsCode(genericWiseDrugArralist.get(drugSpinner.getSelectedItemPosition()).getDrugCode());
            drugRVModel.setStrength(strengthArraylist.get(strengthSpinner.getSelectedItemPosition()).getStrengthName());
            drugRVModel.setStrengthCode(strengthArraylist.get(strengthSpinner.getSelectedItemPosition()).getStrengthCode());
            drugRVModel.setDays(daysArraylist.get(daysSpinner.getSelectedItemPosition()));
            drugRVModel.setTimes(signature.getText().toString());
            drugRVModelArrayList.add(drugRVModel);
            drugsAdapter.notifyDataSetChanged();
            daysSpinner.setSelection(0);
            drugSpinner.setSelection(0);
            genericSpinner.setSelection(0);
            strengthSpinner.setSelection(0);
        }
    }

    public void submit(View view) {
        String diagnosisField = diagnosis.getText().toString();
        String findingsField = findings.getText().toString();
        String drugSignature = signature.getText().toString();
        if (diagnosisField.length() == 0)
            AlertDialogManager.showMissingDialog(DoctorPrescriptionActivity.this, "Diagnosis Field Missing");
        else if (findingsField.length() == 0)
            AlertDialogManager.showMissingDialog(DoctorPrescriptionActivity.this, "Findings Field Missing");
        else if (drugSignature.length() == 0)
            AlertDialogManager.showMissingDialog(DoctorPrescriptionActivity.this, "Signature Field Missing");
        else {
            ArrayList<String> sym = new ArrayList<>();
            ArrayList<String> test = new ArrayList<>();

            for (int i = 0; i < labTestModelArrayList.size(); i++) {
                sym.add(labTestModelArrayList.get(i).getSymptomCode());
                test.add(labTestModelArrayList.get(i).getTestCode());
            }


            savePatientHealthInfoPostAPICall(mConsultationId, findingsField, diagnosisField, drugSignature);


        }


    }

    //    {"DrConsultationCode":"CI-00000002",
//            "Findings":"Findings",
//            "Diagnosis":"Diagnosis",
//            "NextFollowUpDate":"2018-03-04",
//            "SI_SymptomCode":["DI_001","DI_002"],
//        "LT_LabTestCode":["LT001","DI_001"],
//        "DrugSignature":"",
//            "DI_DrugCode":["SC001","DI_001"],
//        "DI_Strength":["DI_001","DI_002"],
//        "DI_Days":["25","54"]

    /*     jsonObject.put("DrConsultationCode", "00000002");
           jsonObject.put("Findings", "Findings");
           jsonObject.put("Diagnosis", "Diagnosis");
           jsonObject.put("NextFollowUpDate", "2018-03-04");
           jsonObject.put("DrugSignature", "");*/
//    }
    private void savePatientHealthInfoPostAPICall(final String consultationId, String findingsField, String diagnosisField, String drugSignature) {


        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("DrConsultationCode", consultationId);
            jsonObject.put("Findings", findingsField);
            jsonObject.put("Diagnosis", diagnosisField);
            jsonObject.put("NextFollowUpDate", "2018-03-04");
            jsonObject.put("DrugSignature", drugSignature);


            // jsonArraySymptom
            JSONArray jsonArraySymptom = new JSONArray();

            for (int i = 0; i < labTestModelArrayList.size(); i++) {

                jsonArraySymptom.put(labTestModelArrayList.get(i).getSymptomCode());
            }

            jsonObject.put("SI_SymptomCode", jsonArraySymptom);


            //jsonArrayLabTestCode
            JSONArray jsonArrayLabTestCode = new JSONArray();

            for (int i = 0; i < labTestModelArrayList.size(); i++) {

                jsonArrayLabTestCode.put(labTestModelArrayList.get(i).getTestCode());
            }

            jsonObject.put("LT_LabTestCode", jsonArrayLabTestCode);


            //jsonArrayDIDrugCode
            JSONArray jsonArrayDIDrugCode = new JSONArray();

            for (int i = 0; i < drugRVModelArrayList.size(); i++) {

                jsonArrayDIDrugCode.put(drugRVModelArrayList.get(i).getDrugsCode());
            }


            jsonObject.put("DI_DrugCode", jsonArrayDIDrugCode);

            //jsonArrayDIStrength

            JSONArray jsonArrayDIStrength = new JSONArray();

            for (int i = 0; i < drugRVModelArrayList.size(); i++) {

                jsonArrayDIStrength.put(drugRVModelArrayList.get(i).getStrengthCode());
            }

            jsonObject.put("DI_Strength", jsonArrayDIStrength);


            //jsonArrayDIDays

            JSONArray jsonArrayDIDays = new JSONArray();

            for (int i = 0; i < drugRVModelArrayList.size(); i++) {


                jsonArrayDIDays.put(drugRVModelArrayList.get(i).getDays());
            }
            jsonObject.put("DI_Days", jsonArrayDIDays);


            Log.e("Json", jsonObject.toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    "http://180.148.210.139:8081/pulse_api/api/savedoctorPrescriptioninfo", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("Json_reques", response.toString());
                            //  YOUR RESPONSE
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e("error_js", error.getMessage());

                }
            });

            AppController.getInstance().addToRequestQueue(jsonObjReq, "tag");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void callDoctorInfo() {
        String url = "http://180.148.210.139:8081/pulse_api/api/getdoctorProfileView/";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + AppSharePreference.getDoctorID(this),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            String jsonObjectInside;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_DrName");
                                doc_press_DocName.setText(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_Phone");
                                doc_pres_doc_number.setText(jsonObjectInside);
                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_Email");
                                doc_pres_doc_email.setText(jsonObjectInside);

                                jsonObjectInside = jsonArray.getJSONObject(i).getString("DRI_DrID");
                                doc_pres_doc_id.setText(jsonObjectInside);


                                jsonObjectInside = jsonArray.getJSONObject(i).getString("Photo");
                                getDoctorImageRequest(jsonObjectInside, doc_pres_iv_DocImage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getDoctorImageRequest(String imageLink, final ImageView imageView) {


// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(AppConfig.LIVE_IMAGE_DOCTOR_API_LINK + imageLink,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
//                        pDialog.hide();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.ic_doctor);
//                        pDialog.hide();
                    }
                });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(request);
    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();

    }
}
