package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/6/2018.
 */

public class DateString {
    private String today;

    public DateString() {
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public DateString(String today) {
        this.today = today;
    }
}
