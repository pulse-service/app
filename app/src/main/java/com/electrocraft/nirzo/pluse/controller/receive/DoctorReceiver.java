package com.electrocraft.nirzo.pluse.controller.receive;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.electrocraft.nirzo.pluse.R;
import com.electrocraft.nirzo.pluse.controller.application.AppConfig;
import com.electrocraft.nirzo.pluse.controller.application.AppController;
import com.electrocraft.nirzo.pluse.controller.network.NotifySMSReceived;
import com.electrocraft.nirzo.pluse.model.AppointmentModel;
import com.electrocraft.nirzo.pluse.view.fragment.PtPaymentModuleFragment;
import com.electrocraft.nirzo.pluse.view.util.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nirzo on 3/17/2018.
 */

public class DoctorReceiver extends BroadcastReceiver {
    private String TAG = "DoctorReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotifySMSReceived.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
//        Toast.makeText(context, "HEllo", Toast.LENGTH_SHORT).show();

//        showRiningMessage(context.getApplicationContext());
    }


}
