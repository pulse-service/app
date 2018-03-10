package com.electrocraft.nirzo.pluse.model.dba;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nirzo on 3/10/2018.
 */
@Table(name = "PatientUser")
public class PatientUser extends Model {
    @Column(name = "pri_PTID")
    public String pri_PTID = "";
    @Column(name = "pri_PTName")
    public String pri_PTName = "";
    @Column(name = "acs_CountryCode")
    public String acs_CountryCode = "";
    @Column(name = "pri_Phone")
    public String pri_Phone = "";
    @Column(name = "pri_Email")
    public String pri_Email = "";

    public PatientUser() {
        super();
    }
}
