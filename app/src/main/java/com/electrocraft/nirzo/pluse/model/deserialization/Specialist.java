package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/6/2018.
 */

public class Specialist {
    private int specId;
    private String specName;

    public Specialist() {
    }

    public Specialist(int specId, String specName) {
        this.specId = specId;
        this.specName = specName;
    }


    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }
}
