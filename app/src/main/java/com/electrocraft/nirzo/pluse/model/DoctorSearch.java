package com.electrocraft.nirzo.pluse.model;

/**
 * @author Faisal Mohammad
 * @since 2/22/2018.
 */

public class DoctorSearch {
    private String name;
    private String expertise;
    private String specialization;
    private String amount;
    private boolean availableFlag;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public DoctorSearch(String name, String expertise, String specialization, String amount, boolean availableFlag) {
        this.name = name;
        this.specialization = specialization;
       /* "Expertise": "FCPS, FRCS, MD (MED)",
                "SPName": "Cardiology",*/
        this.expertise = expertise;
        this.amount = amount;
        this.availableFlag = availableFlag;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public DoctorSearch(String name, String expertise, boolean availableFlag) {
        this.name = name;
        this.expertise = expertise;
        this.availableFlag = availableFlag;
    }

    public boolean isAvailableFlag() {
        return availableFlag;
    }

    public void setAvailableFlag(boolean availableFlag) {
        this.availableFlag = availableFlag;
    }
}
