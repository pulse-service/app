package com.electrocraft.nirzo.pluse.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorHomeActivity;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DoctorPrescription;
import com.electrocraft.nirzo.pluse.view.activity.patient.PatientHomeActivity;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private JitsiMeetView jitsiMeetView;

    private boolean isPatient;

//    @Override
//    public void onBackPressed() {
//        if (!JitsiMeetView.onBackPressed()) {
//            // Invoke the default handler if it wasn't handled by React.
//            super.onBackPressed();
//
//
//        }
//        startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameJitsiMeet = findViewById(R.id.frameJitsiMeet);
        isPatient = getIntent().getBooleanExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);

        jitsiMeetView = new JitsiMeetView(this);

        frameJitsiMeet.addView(jitsiMeetView);

        jitsiMeetView.setListener(new JitsiMeetViewAdapter() {
            @Override
            public void onConferenceFailed(Map<String, Object> data) {
                super.onConferenceFailed(data);
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConferenceJoined(Map<String, Object> data) {
                super.onConferenceJoined(data);
//                Toast.makeText(MainActivity.this, "Joined", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConferenceLeft(Map<String, Object> data) {
                super.onConferenceLeft(data);

                Intent intent;
                if (isPatient)
                    intent= new Intent(MainActivity.this, PatientHomeActivity.class);
                else
                    intent= new Intent(MainActivity.this, DoctorPrescription.class);

                startActivity(intent);
            }

            @Override
            public void onConferenceWillJoin(Map<String, Object> data) {
                super.onConferenceWillJoin(data);
//                Toast.makeText(MainActivity.this, "Join", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConferenceWillLeave(Map<String, Object> data) {
                super.onConferenceWillLeave(data);
//                Toast.makeText(MainActivity.this, "Leave", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadConfigError(Map<String, Object> data) {
                super.onLoadConfigError(data);
            }
        });


        try {
            // jitsiMeetView.loadURL(new URL("https://180.148.210.139/Pulse"));
            jitsiMeetView.loadURL(new URL("https://meet.jit.si/Pulse"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        jitsiMeetView.dispose();
        jitsiMeetView = null;

        JitsiMeetView.onHostDestroy(this);

  /*      Intent intent;
        if (isPatient)
            intent= new Intent(MainActivity.this, PatientHomeActivity.class);
        else
            intent= new Intent(MainActivity.this, DoctorHomeActivity.class);

            startActivity(intent);*/
    }

    @Override
    public void onNewIntent(Intent intent) {
        JitsiMeetView.onNewIntent(intent);
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


    @Override
    protected void onPause() {
        super.onPause();
    }
}
