package com.electrocraft.nirzo.pluse.model.deserialization;

/**
 * Created by nirzo on 3/6/2018.
 */

public class CurrentDate {
    private String currentMonth;
    private String currentYear;

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public CurrentDate() {
    }
}
