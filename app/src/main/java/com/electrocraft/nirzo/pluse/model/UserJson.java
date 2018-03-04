package com.electrocraft.nirzo.pluse.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirzo on 3/4/2018.
 */

public class UserJson {
    @SerializedName("mss")
    private String mss;
    @SerializedName("result")
    private Result[] result;

    public String getMss() {
        return mss;
    }

    public void setMss(String mss) {
        this.mss = mss;
    }

    public Result[] getResult() {
        return result;
    }

    public void setResult(Result[] result) {
        this.result = result;
    }
}
