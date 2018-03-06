package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/6/2018.
 */

public class Doctor {
    private String name;
    private Degree[] degree;
    private Specialist specialist;

    private DateString todayStr;
    private DocAppointments[] appointments;



    public Doctor() {
    }

    public DocAppointments[] getAppointments() {
        return appointments;
    }

    public void setAppointments(DocAppointments[] appointments) {
        this.appointments = appointments;
    }



    public DateString getTodayStr() {
        return todayStr;
    }

    public void setTodayStr(DateString todayStr) {
        this.todayStr = todayStr;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Degree[] getDegree() {
        return degree;
    }

    public void setDegree(Degree[] degree) {
        this.degree = degree;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }
}
