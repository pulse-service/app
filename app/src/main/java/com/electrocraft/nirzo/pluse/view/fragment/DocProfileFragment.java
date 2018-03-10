package com.electrocraft.nirzo.pluse.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.model.SpinnerHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nirzo on 3/1/2018.
 */

public class DocProfileFragment extends Fragment {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
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

    @OnClick(R.id.ivCamera)
    public void onCameraClick() {
        cameraIntent();
    }

    @OnClick(R.id.editDocDOB)
    public void getONText() {
        setDate();
    }

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
        loadBloodGroup();
        loadNationality();
        loadLanguage();
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

    /**
     * load spinner of Blood Group
     */
    private void loadBloodGroup() {
        List<SpinnerHelper> list = new ArrayList<>();
        SpinnerHelper helper;

        //todo get method apply
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
        helper = new SpinnerHelper(7, "007", "AB+");
        list.add(helper);
        helper = new SpinnerHelper(8, "008", "AB-");
        list.add(helper);

        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spBloodGroup.setAdapter(adapter);
    }


    private void loadNationality() {
        List<SpinnerHelper> list = new ArrayList<>();
        SpinnerHelper helper;

        //todo get method apply
        helper = new SpinnerHelper(1, "001", "Bangladeshi");
        list.add(helper);
        helper = new SpinnerHelper(2, "002", "Indian");
        list.add(helper);


        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.rsc_spinner_text, list);

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        spDocNationality.setAdapter(adapter);
    }


    private void loadLanguage() {
        List<SpinnerHelper> list = new ArrayList<>();
        SpinnerHelper helper;

        //todo get method apply
        helper = new SpinnerHelper(1, "001", "English");
        list.add(helper);
        helper = new SpinnerHelper(2, "002", "Bengali");
        list.add(helper);


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
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
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
