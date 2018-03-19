package com.electrocraft.nirzo.pluse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private JitsiMeetView view;

    private boolean isPatient;

    @Override
    public void onBackPressed() {
        if (!JitsiMeetView.onBackPressed()) {
            // Invoke the default handler if it wasn't handled by React.
            super.onBackPressed();

            startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isPatient = getIntent().getBooleanExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);

        view = new JitsiMeetView(this);
        try {
            // view.loadURL(new URL("https://180.148.210.139/Pulse"));
            view.loadURL(new URL("https://meet.jit.si/Pulse"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        view.dispose();
        view = null;

        JitsiMeetView.onHostDestroy(this);

        Intent intent;
        if (isPatient)
            intent= new Intent(MainActivity.this, PatientHomeActivity.class);
        else
            intent= new Intent(MainActivity.this, DoctorHomeActivity.class);

            startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        JitsiMeetView.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        JitsiMeetView.onHostPause(this);
    }

}
