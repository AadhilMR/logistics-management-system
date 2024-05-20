package com.aadhil.ejb.remote;

import com.aadhil.ejb.entity.User;
import jakarta.ejb.Remote;

@Remote
public interface LoginService {
    User login(String username, String password);
}
