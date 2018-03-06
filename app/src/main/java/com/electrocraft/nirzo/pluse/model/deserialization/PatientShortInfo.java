package com.electrocraft.nirzo.pluse.model.deserialization;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nirzo on 3/6/2018.
 */

@Table(name = "PatientShortInfos")
public class PatientShortInfo extends Model {

    @Column(name = "Name")
    private String name;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "Problem")
    private String prob;

    public PatientShortInfo(String name, String sex, String prob) {
        this.name = name;
        this.sex = sex;
        this.prob = prob;
    }


    public PatientShortInfo() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProb() {
        return prob;
    }

    public void setProb(String prob) {
        this.prob = prob;
    }

}
