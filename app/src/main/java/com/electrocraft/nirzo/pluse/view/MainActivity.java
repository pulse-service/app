package com.electrocraft.nirzo.pluse.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.R;

import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private JitsiMeetView view;

    @Override
    public void onBackPressed() {
        if (!JitsiMeetView.onBackPressed()) {
            // Invoke the default handler if it wasn't handled by React.
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new JitsiMeetView(this);
        try {
            // view.loadURL(new URL("https://180.148.210.139/Pulse"));
            view.loadURL(new URL("https://meet.jit.si/Pulse"));
        }
        catch (MalformedURLException e) {
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
