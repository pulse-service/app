package com.electrocraft.nirzo.pluse.view.activity.doctor;

/**
 * Created by wasiun on 3/19/18.
 */

public class StrengthModel {
    private String strengthName;
    private String strengthCode;

    public String getStrengthName() {
        return strengthName;
    }

    public void setStrengthName(String strengthName) {
        this.strengthName = strengthName;
    }

    public String getStrengthCode() {
        return strengthCode;
    }

    public void setStrengthCode(String strengthCode) {
        this.strengthCode = strengthCode;
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return strengthName;
    }
}
