package com.electrocraft.nirzo.pluse.model;

/**
 * Created by nirzo on 3/25/2018.
 */

public class Prescription {
    private String PRI_PTID;
    private String DRI_DrID;
    private String PI_DoctorPrescriptionCode;
    private String CI_ConsultationDate;
    private String DRI_DrName;

    public String getPRI_PTID() {
        return PRI_PTID;
    }

    public void setPRI_PTID(String PRI_PTID) {
        this.PRI_PTID = PRI_PTID;
    }

    public String getDRI_DrID() {
        return DRI_DrID;
    }

    public void setDRI_DrID(String DRI_DrID) {
        this.DRI_DrID = DRI_DrID;
    }

    public String getPI_DoctorPrescriptionCode() {
        return PI_DoctorPrescriptionCode;
    }

    public void setPI_DoctorPrescriptionCode(String PI_DoctorPrescriptionCode) {
        this.PI_DoctorPrescriptionCode = PI_DoctorPrescriptionCode;
    }

    public String getCI_ConsultationDate() {
        return CI_ConsultationDate;
    }

    public void setCI_ConsultationDate(String CI_ConsultationDate) {
        this.CI_ConsultationDate = CI_ConsultationDate;
    }

    public String getDRI_DrName() {
        return DRI_DrName;
    }

    public void setDRI_DrName(String DRI_DrName) {
        this.DRI_DrName = DRI_DrName;
    }

    public Prescription(String PRI_PTID, String DRI_DrID, String PI_DoctorPrescriptionCode, String CI_ConsultationDate, String DRI_DrName) {
        this.PRI_PTID = PRI_PTID;
        this.DRI_DrID = DRI_DrID;
        this.PI_DoctorPrescriptionCode = PI_DoctorPrescriptionCode;
        this.CI_ConsultationDate = CI_ConsultationDate;
        this.DRI_DrName = DRI_DrName;
    }
    /*    "PRI_PTID": "ECL-00000001",
            "DRI_DrID": "DR000000001",
            "PI_DoctorPrescriptionCode": "DP-00000023",
            "CI_ConsultationDate": "2018-03-15",
            "DRI_DrName": "Dr. Farzana B Ibrahim"*/
}
