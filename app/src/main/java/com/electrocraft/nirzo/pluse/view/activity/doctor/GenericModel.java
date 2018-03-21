package com.electrocraft.nirzo.pluse.view.activity.doctor;

/**
 * Created by wasiun on 3/19/18.
 */

public class GenericModel {
    private String InfoCode;
    private String GenericName;

    public String getInfoCode() {
        return InfoCode;
    }

    public void setInfoCode(String infoCode) {
        InfoCode = infoCode;
    }

    public String getGenericName() {
        return GenericName;
    }

    public void setGenericName(String genericName) {
        GenericName = genericName;
    }
    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return GenericName;
    }
}
