package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/21/2018.
 */

public class SpecilizationType {
    private String spTypeCode;
    private String spTypeName;

    public String getSpTypeCode() {
        return spTypeCode;
    }

    public void setSpTypeCode(String spTypeCode) {
        this.spTypeCode = spTypeCode;
    }

    public String getSpTypeName() {
        return spTypeName;
    }

    public void setSpTypeName(String spTypeName) {
        this.spTypeName = spTypeName;
    }

    public SpecilizationType(String spTypeCode, String spTypeName) {

        this.spTypeCode = spTypeCode;
        this.spTypeName = spTypeName;
    }
}
