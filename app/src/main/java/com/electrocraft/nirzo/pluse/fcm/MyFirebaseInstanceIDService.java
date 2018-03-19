package com.electrocraft.nirzo.pluse.fcm;

import android.util.Log;


import com.electrocraft.nirzo.pluse.controller.util.SharePref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Nazrul Islam on 7/26/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed token: ", refreshedToken);
        saveFcmToken(refreshedToken);
//        L.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

        Log.d("Refreshed token: ", refreshedToken);
    }

    public void saveFcmToken(String refreshedToken) {
        SharePref.saveFireBaseToken(getApplicationContext(), refreshedToken);

    }
}