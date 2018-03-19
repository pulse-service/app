package com.electrocraft.nirzo.pluse.model;

/**
 * Created by nirzo on 3/17/2018.
 */

public class AppointmentModel {
    private String appointmentDate;
    private String inTime;
    private String inTime_AMOrPM;
    private String doctorID;
    private String doctorName;
    private String patientID;
    private String patientName;
    private String probShortDescribtion;

    public String getProbShortDescribtion() {
        return probShortDescribtion;
    }

    public void setProbShortDescribtion(String probShortDescribtion) {
        this.probShortDescribtion = probShortDescribtion;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public AppointmentModel() {
    }

    public AppointmentModel(String appointmentDate, String inTime, String inTime_AMOrPM, String doctorID, String doctorName) {
        this.appointmentDate = appointmentDate;
        this.inTime = inTime;
        this.inTime_AMOrPM = inTime_AMOrPM;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
    }

    public AppointmentModel(String appointmentDate, String inTime, String inTime_AMOrPM, String patientID, String patientName, String probShortDescribtion) {
        this.appointmentDate = appointmentDate;
        this.inTime = inTime;
        this.inTime_AMOrPM = inTime_AMOrPM;
        this.patientID = patientID;
        this.patientName = patientName;
        this.probShortDescribtion = probShortDescribtion;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getInTime_AMOrPM() {
        return inTime_AMOrPM;
    }

    public void setInTime_AMOrPM(String inTime_AMOrPM) {
        this.inTime_AMOrPM = inTime_AMOrPM;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
