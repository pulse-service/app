package com.electrocraft.nirzo.pluse.controller.receive;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.electrocraft.nirzo.pluse.view.activity.doctor.DocNotifyCallReceivedAlertActivity;




/**
 *
 * @author Faisal
 * @since 3/17/2018
 */

public class DoctorIncomingAlertReceiver extends BroadcastReceiver {
    private String TAG = "DoctorIncomingAlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, DocNotifyCallReceivedAlertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }


}
