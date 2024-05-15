package com.aadhil.ejb.entity;

import java.io.Serializable;

public enum UserType implements Serializable {
    ADMIN, SUPERVISOR, USER;

    public static UserType getUserType(int id) throws RuntimeException {
        if(id==0) {
            return ADMIN;
        } else if(id==1) {
            return SUPERVISOR;
        } else if(id==2) {
            return USER;
        } else {
            throw new RuntimeException("Parameter not in range");
        }
    }
}
