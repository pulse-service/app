package com.electrocraft.nirzo.pluse.controller.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.electrocraft.nirzo.pluse.controller.util.LruBitmapCache;
import com.electrocraft.nirzo.pluse.view.util.Key;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import timber.log.Timber;

/**
 * <p>
 * The Application class in Android is the base class within an Android app that contains all
 * other components such as activities and services.
 * </p>
 *
 * @author Faisal
 * @since 2/26/2018.
 */

public class AppController extends com.activeandroid.app.Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Timber.plant(new Timber.DebugTree());

        printHashKey(getApplicationContext());

//        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));
//        AndroidNetworking.initialize(getApplicationContext());
        // todo: eta ke single class e ante hobe
        SharedPreferences preferences = getSharedPreferences(Key.APP_PREFERENCE, MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean(Key.KEY_IS_FIRST_TIME, false);

        if (!isFirstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Key.KEY_IS_FIRST_TIME,true);
            editor.apply();
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
