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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.util.ImageFilePath;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import timber.log.Timber;

/**
 * Created by nirzo on 3/1/2018.
 */

public class DocProfileFragment extends Fragment {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    private static final int PICK_IMAGE_REQUEST = 3;
    @BindView(R.id.edtDocFirstName)
    EditText edtDocFirstName;

    @BindView(R.id.edtDocLastName)
    EditText edtDocLastName;

    @BindView(R.id.sp_DocBloodGroup)
    Spinner spBloodGroup;

    @BindView(R.id.sp_DocNationality)
    Spinner spDocNationality;


    @BindView(R.id.sp_DocLanguage)
    Spinner spDocLanguage;

    @BindView(R.id.iv_doc_image_thumbel)
    ImageView ivImage;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();
    @BindView(R.id.editDocDOB)
    public TextView reDOB;

    @BindView(R.id.ivCamera)
    public ImageView ivCamera;
    private ProgressDialog progressDialog;
    private ProgressDialog pDialog;
    private String mToken = "";

    @OnClick(R.id.ivCamera)
    public void onCameraClick() {
        cameraIntent();
    }

    @OnClick(R.id.editDocDOB)
    public void getONText() {
        setDate();
    }


    String imageFilePath;

    public DocProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_doc_profile, container, false);
        ButterKnife.bind(this, view);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        getBloodGroup();
        getNationality();
        getLanguage();



        return view;
    }

    /**
     * DatePicker code Start
     **/
    public void updateDate() {
        reDOB.setText(format.format(calendar.getTime()));
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
        }
    };





    private void getBloodGroup() {
        String blood_group_tag = "blood_group_tag";
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getbloodgroup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Timber.d(response);

                        String BGCode = "";
                        String BGName = "";


                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);
                            List<SpinnerHelper> bloodGroupList = new ArrayList<>();
                            if (!object.isNull("data")) {

                                JSONArray array = object.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    BGCode = jsonObject.getString("BGCode");
                                    BGName = jsonObject.getString("BGName");

                                    SpinnerHelper helper = new SpinnerHelper(i, BGCode, BGName);
                                    bloodGroupList.add(helper);

                                }
                                loadBloodGroup(bloodGroupList);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                closeDialog();                                                                          // hide the progress dialog
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, blood_group_tag);

    }

    /**
     * hide the progress dialog
     */
    private void closeDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.hide();
    }

    private void getNationality() {
        String blood_group_tag = "blood_group_tag";
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getnationality",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Timber.d(response);

                        String nCode = "";
                        String nName = "";

                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);
                            List<SpinnerHelper> list = new ArrayList<>();
                            if (!object.isNull("data")) {

                                JSONArray array = object.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    nCode = jsonObject.getString("nCode");
                                    nName = jsonObject.getString("nName");

                                    SpinnerHelper helper = new SpinnerHelper(i, nCode, nName);
                                    list.add(helper);

                                }
                                loadNationality(list);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                pDialog.hide();                                                                           // hide the progress dialog
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, blood_group_tag);

    }


    private void getLanguage() {
        String blood_group_tag = "language_tag";
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.LIVE_API_LINK + "getlanguagelist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AppController.getInstance().getRequestQueue().getCache().clear();
                        Timber.d(response);

                        String lanCode = "";
                        String lanName = "";

                        closeDialog();
                        try {
                            JSONObject object = new JSONObject(response);
                            List<SpinnerHelper> list = new ArrayList<>();
                            if (!object.isNull("data")) {

                                JSONArray array = object.getJSONArray("data");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    lanCode = jsonObject.getString("lanCode");
                                    lanName = jsonObject.getString("lanName");

                                    SpinnerHelper helper = new SpinnerHelper(i, lanCode, lanName);
                                    list.add(helper);

                                }
                                loadLanguage(list);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Timber.d("Error: " + error.getMessage());
                pDialog.hide();                                                                           // hide the progress dialog
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, blood_group_tag);

    }

    /**
     * load spinner of Blood Group
     */
    private void loadBloodGroup(List<SpinnerHelper> list) {


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spBloodGroup.setAdapter(adapter);
    }


    private void loadNationality(List<SpinnerHelper> list) {


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spDocNationality.setAdapter(adapter);
    }


    private void loadLanguage(List<SpinnerHelper> list) {

        closeDialog();

        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spDocLanguage.setAdapter(adapter);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @OnClick(R.id.ivFolder)
    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_REQUEST);
    }


    public void imageBrowse() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start  the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

    }

    Bitmap bitmap;

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

    @OnClick(R.id.btn_symptom_submit)
    public void onSummit(View view) {
        imageUpload(imageFilePath);
    }

    public void imageUpload(final String imagePath) {


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        //converting image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.IMAGE_UPLOAD_API_LINK, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("MORDIM", s);
                progressDialog.dismiss();
        /*        if (s.equals("true")) {
                    Toast.makeText(getActivity(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                return parameters;
            }
        };


        AppController.getInstance().addToRequestQueue(request);
    }


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
}
