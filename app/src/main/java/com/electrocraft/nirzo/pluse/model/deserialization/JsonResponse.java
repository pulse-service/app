package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/6/2018.
 */

public class JsonResponse {
    private boolean error_flag;
    private String error_info;
    private Doctor doctor;

    public boolean isError_flag() {
        return error_flag;
    }

    public void setError_flag(boolean error_flag) {
        this.error_flag = error_flag;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public JsonResponse() {
    }

}
