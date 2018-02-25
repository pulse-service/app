package com.electrocraft.nirzo.pluse.model;

/**
 * Created by nirzo on .
 *
 * @author Faisal Mohammad
 * @since 2/25/2018
 */

public class GeoLayR4Location {

    private String acsCountryCode;
    private String geoLayRCode;
    private String layR1ListCode;
    private String layR2ListCode;
    private String layR3ListCode;
    private String layR4ListCode;
    private String layR4ListName;

    public GeoLayR4Location(String acsCountryCode, String geoLayRCode, String layR1ListCode,
                            String layR2ListCode, String layR3ListCode, String layR4ListCode,
                            String layR4ListName) {
        this.acsCountryCode = acsCountryCode;
        this.geoLayRCode = geoLayRCode;
        this.layR1ListCode = layR1ListCode;
        this.layR2ListCode = layR2ListCode;
        this.layR3ListCode = layR3ListCode;
        this.layR4ListCode = layR4ListCode;
        this.layR4ListName = layR4ListName;
    }

    public GeoLayR4Location(String geoLayRCode, String layR4ListName) {
        this.geoLayRCode = geoLayRCode;
        this.layR4ListName = layR4ListName;
    }

    public String getAcsCountryCode() {
        return acsCountryCode;
    }

    public String getGeoLayRCode() {
        return geoLayRCode;
    }

    public String getLayR1ListCode() {
        return layR1ListCode;
    }

    public String getLayR2ListCode() {
        return layR2ListCode;
    }

    public String getLayR3ListCode() {
        return layR3ListCode;
    }

    public String getLayR4ListCode() {
        return layR4ListCode;
    }

    public String getLayR4ListName() {
        return layR4ListName;
    }
}
