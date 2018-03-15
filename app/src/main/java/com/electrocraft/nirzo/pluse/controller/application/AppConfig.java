package com.electrocraft.nirzo.pluse.controller.application;

/**
 * Created by nirzo on 3/4/2018.
 */

public class AppConfig {

    /**
     * Habib's pc server host
     */
//    public static final String API_LINK = "http://192.168.1.7/elc_api/public/api/auth/";
    /**
     * Hemonto Dada pc
     */
    public static final String API_LINK = "http://192.168.1.116:8080/elc_api/public/api/";


    /************************************************************************************
     *--------------------------  LIVE Server ------------------------------------------
     ***********************************************************************************/
    public static final String LIVE_API_LINK = "http://180.148.210.139:8081/pulse_api/api/";
    public static final String LIVE_IMAGE_DOCTOR_API_LINK = "http://180.148.210.139:8081/pulse_api/public/Doctor_profile_photo/";

    public static final String API_NOT_AVALIBLE = "wtf";


    public static final String IMAGE_UPLOAD_API_LINK = "http://192.168.1.110:82/pic_up/upload.php";

    /**
     *  jodi otp sms bondh korte chai taile SMS_OTP_OFF ta true kore dite hobe
     *  sms deactivate  korte chaile
     */
    public static final boolean SMS_OTP_OFF = false;
}
