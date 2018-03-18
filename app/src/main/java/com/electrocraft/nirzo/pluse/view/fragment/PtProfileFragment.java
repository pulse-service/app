package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.ImageFilePath;
import com.electrocraft.nirzo.pluse.view.notification.AlertDialogManager;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * This class is to update  patient profile
 *
 * @author Faisal Mohammad
 * @since 2/25/2018.
 */

public class PtProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 3;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    /**
     * the patient id
     */
    private String mPatientId;

    @BindView(R.id.iv_doc_image_thumbel)
    ImageView ivImage;

    private String mfatherName = "";
    private String mMotherName = "";
    private String mPresentAddress = "";
    private String mPatientDateOfBirth = "";
    private String mAge = "";
    /**
     * save button
     */
    @BindView(R.id.btn_PtPersonalInfoSave)
    Button btn_PtPersonalInfoSave;


    @BindView(R.id.edtPtFatherName)
    EditText edtPtFatherName;

    @BindView(R.id.edtPtMotherName)
    EditText edtPtMotherName;


    @BindView(R.id.edtPtPresentAddress)
    EditText edtPtPresentAddress;


    @BindView(R.id.tvPtAge)
    TextView tvPtAge;
    private ProgressDialog pDialog;


    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();
    @BindView(R.id.tvPatientDOB)
    public TextView tvPatientDOB;
    private String imageFilePath;

    /**
     * DatePicker code Start
     **/
    public void updateDate() {
        tvPatientDOB.setText(format.format(calendar.getTime()));
    }

    public void setDate() {
        new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
            tvPtAge.setText(getAge(year, monthOfYear, dayOfMonth));
        }
    };


    @OnClick(R.id.tvPatientDOB)
    public void patientDobClick() {
        setDate();
    }

    public PtProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_pt_profile, container, false);
        ButterKnife.bind(this, view);
        mPatientId = getArguments().getString(Key.KEY_PATIENT_ID, "");

        tvPtAge.setText("0");
        getPatientPersonalInfo(mPatientId);


        return view;
    }

    /**
     * set data to the View
     *
     * @param str  data
     * @param view editText
     */
    private void setDataToView(String str, EditText view) {
        if (str.length() > 0)
            view.setText(str);
        else
            view.setText("");
    }

    private void setPatientProfileInfo() {

        setDataToView(mfatherName, edtPtFatherName);
        setDataToView(mMotherName, edtPtMotherName);
        setDataToView(mPresentAddress, edtPtPresentAddress);

        tvPatientDOB.setText(mPatientDateOfBirth);
        tvPtAge.setText(mAge);


    }

    @OnClick(R.id.btn_PtPersonalInfoSave)
    public void onProfileSave(View view) {
        String fatherName = edtPtFatherName.getText().toString();
        String motherName = edtPtMotherName.getText().toString();
        String presentAddress = edtPtPresentAddress.getText().toString();
        String patientDateOfBirth = tvPatientDOB.getText().toString();
        String age = tvPtAge.getText().toString();

        savePatientPersonalInfo(mPatientId, fatherName, motherName, patientDateOfBirth, age, presentAddress);
        saveProfileAccount(mPatientId, fatherName, motherName, patientDateOfBirth, age, presentAddress);
    }

    /**
     * @param year  patient's year of Birth
     * @param month patient's month of Birth
     * @param day   patient's day of Birth
     * @return age in year
     */
    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        return ageInt.toString();


    }

    private void getPatientPersonalInfo(final String patientId) {
        String tag = "get_patient_personal_info";

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "patientprofilepersonalinfo/" + patientId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();

                        String PPPI_PTPersonalInfoCode = "";
                        String PRI_PTID = "";
                        String PPPI_FatherName = "";
                        String PPPI_MotherName = "";
                        String DOB = "";
                        String PPPI_Age = "";
                        String PPPI_PresentAddress = "";
                        String PPPI_Photo = "";
                        closeDialog();
                        try {
                            JSONObject jos = new JSONObject(response);

                            if (jos.getString("status").equals("success")) {
                                if (!jos.isNull("data")) {
                                    JSONArray daArray = jos.getJSONArray("data");
                                    for (int i = 0; i < daArray.length(); i++) {
                                        JSONObject object = daArray.getJSONObject(i);
                                        PPPI_PTPersonalInfoCode = object.getString("PPPI_PTPersonalInfoCode");
                                        PRI_PTID = object.getString("PRI_PTID");
                                        PPPI_FatherName = object.getString("PPPI_FatherName");
                                        PPPI_MotherName = object.getString("PPPI_MotherName");
                                        DOB = object.getString("DOB");
                                        PPPI_Age = object.getString("PPPI_Age");
                                        PPPI_PresentAddress = object.getString("PPPI_PresentAddress");
                                        PPPI_Photo = object.getString("PPPI_Photo");

                                    }
                                }

                            }


                            mfatherName = PPPI_FatherName;
                            mMotherName = PPPI_MotherName;
                            mPresentAddress = PPPI_PresentAddress;
                            mPatientDateOfBirth = DOB;
                            mAge = PPPI_Age;
                            setPatientProfileInfo();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("PatientPersonal", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());

                closeDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }


    private void savePatientPersonalInfo(final String patientId, final String fatherName, final String motherName,
                                         final String patientDOB, final String patientAge, final String patientPresentAdd) {
        String tag = "patient_personal_info_save";

        if (pDialog == null)
            pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "patientprofilepersonalinfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        String id = "";
                        String msg = "";
                        closeDialog();
                        try {
                            JSONObject jos = new JSONObject(response);

                            msg = jos.getString("msg");


                            AlertDialogManager.showSuccessDialog(getActivity(), msg);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("More", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());

                closeDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("father_name", fatherName);
                params.put("mother_name", motherName);
                params.put("dob", patientDOB);
                params.put("age", patientAge);
                params.put("presentAddress", patientPresentAdd);
                params.put("pri_prid", patientId);
                // api param active but not integrated 
                // params.put("image", image);


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

    private void saveProfileAccount(final String patientId, final String fatherName, final String motherName,
                                    final String patientDOB, final String patientAge, final String patientPresentAdd) {
        // loading or check internet connection or something...
        // ... then
//        String url = "http://www.angga-ari.com/api/something/awesome";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConfig.LIVE_API_LINK + "patientprofilepersonalinfo"
                , new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
         /*           String status = result.getString("status");
                    String message = result.getString("message");*/

                /*    if (status.equals(Constant.REQUEST_SUCCESS)) {
                        // tell everybody you have succed upload image and post strings
                        Log.i("Messsage", message);
                    } else {
                        Log.i("Unexpected", message);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
            /*    params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                params.put("name", mNameInput.getText().toString());
                params.put("location", mLocationInput.getText().toString());
                params.put("about", mAvatarInput.getText().toString());
                params.put("contact", mContactInput.getText().toString());*/
                params.put("father_name", fatherName);
                params.put("mother_name", motherName);
                params.put("dob", patientDOB);
                params.put("age", patientAge);
                params.put("presentAddress", patientPresentAdd);
                params.put("pri_prid", patientId);
                // api param active but not integrated
                // params.put("image", image);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("image", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getActivity(), ivImage.getDrawable()), "image/jpeg"));
//                params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getActivity(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(multipartRequest);
//        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }


    @OnClick(R.id.ivFolder)
    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.ivCamera)
    public void onCameraClick() {
        cameraIntent();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    Bitmap bitmap;

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);


            else if (requestCode == PICK_IMAGE_REQUEST) {


                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    imageFilePath = ImageFilePath.getPath(getActivity(), data.getData());
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        ivImage.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), "Some thing wrong", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}
