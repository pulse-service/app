package com.electrocraft.nirzo.pluse.controller.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nirzo on 3/12/2018.
 */

public class SharePref {
    private static final String FILE_NAME = "pulse_raw_data";
    private static final String KEY_PATIENT_APPOINTMENT_ID = "p_appointment_id";
    private static final String KEY_PATIENT_ID = "p_id";
    private static final String KEY_DOCTOR_ID = "d_id";

    private static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }



    public static void saveAppointmentID(Context context, String value) {
        saveString(context, KEY_PATIENT_APPOINTMENT_ID, value);

    }

    public static String getAppointmentID(Context context) {
        return getString(context, KEY_PATIENT_APPOINTMENT_ID);

    }
    public static void savePatientID(Context context, String value) {
        saveString(context, KEY_PATIENT_ID, value);

    }
    public static void saveDoctorID(Context context, String value) {
        saveString(context, KEY_DOCTOR_ID, value);

    }

    public static String getDoctorID(Context context) {
        return getString(context, KEY_DOCTOR_ID);

    }
    public static String getPatientID(Context context) {
        return getString(context, KEY_PATIENT_ID);

    }

    private static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");

    }

  /*  SharedPreferences preferences = getSharedPreferences(Key.APP_PREFERENCE, MODE_PRIVATE);
    boolean isFirstRun = preferences.getBoolean(Key.KEY_IS_FIRST_TIME, false);*/
}
