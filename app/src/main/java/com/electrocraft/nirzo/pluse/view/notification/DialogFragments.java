package com.electrocraft.nirzo.pluse.view.notification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 *
 *
 * @author Faisal
 * @since 2/22/2018.
 *
 */

public class DialogFragments extends BaseDialogFragment<DialogFragments.OnDialogClickListener> {

    public interface OnDialogClickListener {
        public void onOkClicked(DialogFragments dialog);

        public void onCancelClicked(DialogFragments dialog);
    }

    /**
     * Create an instance of the Dialog with the input
     *
     * @param title   title
     * @param message me
     * @return fragments
     */
    public static DialogFragments newInstance(String title, String message, boolean cancelable) {
        DialogFragments fragments = new DialogFragments();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putBoolean("cancelable", cancelable);
        fragments.setArguments(args);
        return fragments;
    }
    // Create a Dialog using default AlertDialog builder , if not inflate custom view in onCreateView


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString("title", "Title"))
                .setMessage(getArguments().getString("message", "Message"))
                .setCancelable(getArguments().getBoolean("cancelable", false))
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivityInstance().onOkClicked(DialogFragments.this);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivityInstance().onCancelClicked(DialogFragments.this);
                            }
                        })
                .create();
    }

    /**
     *  If you need to use your own custom layout for dialog,then inflate a layout in onCreateView
     *  and remove onCreateDialog . But Add the click listeners in onCreateView like i explained in
     *  onCreateDialog
     */
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog, container, false);
        return view;
    }*/

    /* call by
    * DialogFragments generalDialogFragment =
                    DialogFragments.newInstance("Missing", "Name Missing",false);
            generalDialogFragment.show(getSupportFragmentManager(),"dialog");*/
}
