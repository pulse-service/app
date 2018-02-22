package com.electrocraft.nirzo.pluse.model;

/**
 *
 * @author Faisal Mohammad
 * @since 2/22/2018.
 */

public class DoctorSearch {
    private String name;
    private String institution;
    private boolean availableFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }


    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public DoctorSearch(String name, String institution, boolean availableFlag) {
        this.name = name;
        this.institution = institution;
        this.availableFlag = availableFlag;
    }

    public boolean isAvailableFlag() {
        return availableFlag;
    }

    public void setAvailableFlag(boolean availableFlag) {
        this.availableFlag = availableFlag;
    }
}
