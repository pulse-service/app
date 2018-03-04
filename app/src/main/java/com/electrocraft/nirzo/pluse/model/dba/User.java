package com.electrocraft.nirzo.pluse.model.dba;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by nirzo on 3/4/2018.
 */

@Table(name = "User")
public class User extends Model {

    @Column(name = "UserName")
    public String userName;

    @Column(name = "Password")
    public String password;

    @Column(name = "LoginTime")
    public String logInTime;

    public User() {
        super();
    }
}
