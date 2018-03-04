package com.electrocraft.nirzo.pluse.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirzo on 3/4/2018.
 */

public class Result {
    @SerializedName("name")
    private String name;
    @SerializedName("user_email")
    private String user_email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @Override
    public String toString() {
        return "ClassPojo [name = "+name+", user_email = "+user_email+"]";
    }
}
