package com.electrocraft.nirzo.pluse.controller.application;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.electrocraft.nirzo.pluse.controller.util.LruBitmapCache;
import com.electrocraft.nirzo.pluse.view.util.Key;

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
}
