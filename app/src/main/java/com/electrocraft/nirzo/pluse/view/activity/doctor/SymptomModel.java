package com.electrocraft.nirzo.pluse.view.activity.doctor;

/**
 * Created by wasiun on 3/19/18.
 */

public class SymptomModel {
   private String code;
   private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return name;
    }
}
