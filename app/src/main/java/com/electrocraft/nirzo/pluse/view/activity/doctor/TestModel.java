package com.electrocraft.nirzo.pluse.view.activity.doctor;

/**
 * Created by wasiun on 3/19/18.
 */

public class TestModel {
   private String TestCode;
   private String TestName;
   private String SymptomCode;

    public String getTestCode() {
        return TestCode;
    }

    public void setTestCode(String testCode) {
        TestCode = testCode;
    }

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }

    public String getSymptomCode() {
        return SymptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        SymptomCode = symptomCode;
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return TestName;
    }
}
