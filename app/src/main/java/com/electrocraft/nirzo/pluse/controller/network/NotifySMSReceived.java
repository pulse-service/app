package com.electrocraft.nirzo.pluse.controller.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.electrocraft.nirzo.pluse.view.MainActivity;
import com.electrocraft.nirzo.pluse.view.util.Key;

/**
 * Created by nirzo on 3/17/2018.
 */

public class NotifySMSReceived extends Activity {
    private static final String LOG_TAG = "SMSReceiver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayAlert();
    }

    private void displayAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have a call !").setCancelable(
                false).setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(NotifySMSReceived.this, MainActivity.class);
                        intent.putExtra(Key.KEY_IS_PATIENT_OR_DOCTOR, false);
                        startActivity(intent);
                    }
                }).setNegativeButton("Reject",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
