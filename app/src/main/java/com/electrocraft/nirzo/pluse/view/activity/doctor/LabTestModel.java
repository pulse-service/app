package com.electrocraft.nirzo.pluse.view.activity.doctor;

/**
 * Created by wasiun on 3/19/18.
 */

public class LabTestModel {
    private String Symptom;
    private String SymptomCode;
    private String TestCode;
    private String Test;


    public String getSymptomCode() {
        return SymptomCode;
    }

    public void setSymptomCode(String symptomCode) {
        SymptomCode = symptomCode;
    }

    public String getTestCode() {
        return TestCode;
    }

    public void setTestCode(String testCode) {
        TestCode = testCode;
    }

    public String getSymptom() {
        return Symptom;
    }

    public void setSymptom(String symptom) {
        Symptom = symptom;
    }

    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }


}
