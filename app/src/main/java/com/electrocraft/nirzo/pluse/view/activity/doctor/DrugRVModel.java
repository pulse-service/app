package com.electrocraft.nirzo.pluse.view.activity.doctor;

/**
 * Created by wasiun on 3/19/18.
 */

public class DrugRVModel {


    private String Drugs,DrugsCode;
    private String Strength,StrengthCode;
    private String Days;
    private String Times;


    public String getDrugsCode() {
        return DrugsCode;
    }

    public void setDrugsCode(String drugsCode) {
        DrugsCode = drugsCode;
    }

    public String getStrengthCode() {
        return StrengthCode;
    }

    public void setStrengthCode(String strengthCode) {
        StrengthCode = strengthCode;
    }

    public String getDrugs() {
        return Drugs;
    }

    public void setDrugs(String drugs) {
        Drugs = drugs;
    }

    public String getStrength() {
        return Strength;
    }

    public void setStrength(String strength) {
        Strength = strength;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }
}
