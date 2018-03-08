package com.electrocraft.nirzo.pluse.view.util;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by nirzo on 3/8/2018.
 */

public class Util {
    /**
     *
     * @param email email address string
     * @return true or false
     */
    public static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNo(CharSequence phone){
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches();
    }
}
