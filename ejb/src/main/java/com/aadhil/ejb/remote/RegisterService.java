package com.aadhil.ejb.remote;

import jakarta.ejb.Remote;

@Remote
public interface RegisterService {
    void register(String username, String password, int userTypeId);
}
