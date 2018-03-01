package com.electrocraft.nirzo.pluse.controller.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by nirzo on
 * this class control Assets folder Data
 *
 * @author Faisal
 * @since 3/1/2018.
 */

public class AssetUtils {

    /**
     * this method return json string from assets folder .
     *
     * @param fileName file name of the jason file in assets folder
     * @param context  invoking activity
     * @return json string
     */
    public static String getJsonAsString(String fileName, Context context) throws IOException {

        AssetManager manager = context.getAssets();

        StringBuilder buf = new StringBuilder();
        InputStream jsonStream = manager.open(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(jsonStream, "UTF-8"));

        String str;
        while ((str = br.readLine()) != null) {
            buf.append(str);
        }
        br.close();
        return buf.toString();
    }
}
