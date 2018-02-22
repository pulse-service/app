package com.electrocraft.nirzo.pluse.view.notification;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 *
 *  <p>
 * This class to keep hold of instance of Activity. So  when
 * the dialog attached the activity class will know the instance of
 * activity , which created it  .</p>
 *
 * @author Faisal Mohammad
 * @since 2/22/2018

 */

public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mActivityInstance;



    public final T getActivityInstance() {
        return mActivityInstance;


    }

    @Override
    public void onAttach(Activity activity) {
        mActivityInstance = (T) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityInstance=null;
    }
}
