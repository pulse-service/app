package com.electrocraft.nirzo.pluse.controller.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nirzo on 3/4/2018.
 */

public class ConnectionDetector {
    private Context mContext;

    public ConnectionDetector(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService
                (Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo[] netInfo = manager.getAllNetworkInfo();
            if (netInfo != null) {
                for (NetworkInfo info : netInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
