package com.electrocraft.nirzo.pluse.view.notification;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;


import com.electrocraft.nirzo.pluse.R;


/**
 * Created by nirzon on 2/19/2018.
 *
 * @author Faisal Mohammad
 */

public class AlertDialogManager {

    private static final int ERROR = 0;
    private static final int WARNING = 1;
    private static final int MISSING = 2;
    private static final int SUCCESS = 3;

//    showWarningDialog

    public static void showErrorDialog(Context context, String message) {

        showAcknowledgementDialog(context, "Error", message, "ok", WARNING);

    }

    public static void showMissingDialog(Context context, String message) {

        showAcknowledgementDialog(context, "Missing", message, "ok", MISSING);

    }

    public static void showSuccessDialog(Context context, String message) {

        showAcknowledgementDialog(context, "Success", message, "ok", SUCCESS);

    }

    /**
     * @param context  invoking Context
     * @param title    title of the Dialog
     * @param message  message of the Dialog
     * @param btnLabel acknowledgement Button label
     * @param state    state flag
     */
    private static void showAcknowledgementDialog(Context context, String title, String message,
                                                  String btnLabel, int state) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        alertDialog.setTitle(title);
        alertDialog.setCancelable(true);

        // Setting Dialog Message
        alertDialog.setMessage(message);


        alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        switch (state) {
            case WARNING:
                alertDialog.setIcon(R.drawable.ic_warning_32);
                break;
            case MISSING:
                alertDialog.setIcon(R.drawable.ic_missing_32);
                break;

            case SUCCESS:
                alertDialog.setIcon(R.drawable.ic_checkmark);
                break;
        }


        // Setting  Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, btnLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


}
