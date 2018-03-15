package com.electrocraft.nirzo.pluse.model;

/**
 * Created by nirzo on 3/15/2018.
 */

public class DoctorAvailableTime {

   private String day ;
   private String inTime ;
   private String inTime_AMOrPM ;
   private String outTime;
   private String outTime_AMOrPM ;

    public DoctorAvailableTime(String day, String inTime, String inTime_AMOrPM, String outTime, String outTime_AMOrPM) {
        this.day = day;
        this.inTime = inTime;
        this.inTime_AMOrPM = inTime_AMOrPM;
        this.outTime = outTime;
        this.outTime_AMOrPM = outTime_AMOrPM;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getOutTime_AMOrPM() {
        return outTime_AMOrPM;
    }

    public void setOutTime_AMOrPM(String outTime_AMOrPM) {
        this.outTime_AMOrPM = outTime_AMOrPM;
    }
}
