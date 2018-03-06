package com.electrocraft.nirzo.pluse.model.deserialization;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nirzo on 3/6/2018.
 */
@Table(name = "DocAppointments")
public class DocAppointments extends Model {

    @Column(name = "Isbooked", index = false)
    private boolean isBooked;
    @Column(name = "Time")
    private String time;
    @Column(name = "PatientShortInfo")
    private PatientShortInfo patients;

    public boolean isIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public DocAppointments() {
    }

    public PatientShortInfo getPatients() {
        return patients;
    }

    public void setPatients(PatientShortInfo patients) {
        this.patients = patients;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
