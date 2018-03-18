package com.electrocraft.nirzo.pluse.controller.receive;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.electrocraft.nirzo.pluse.controller.network.NotifySMSReceived;




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

    }


}
