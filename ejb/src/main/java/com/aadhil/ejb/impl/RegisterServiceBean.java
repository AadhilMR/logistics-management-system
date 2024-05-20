package com.aadhil.ejb.impl;

import com.aadhil.ejb.entity.User;
import com.aadhil.ejb.entity.UserType;
import com.aadhil.ejb.remote.RegisterService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class RegisterServiceBean implements RegisterService {
    @PersistenceContext(unitName = "LMS_PU")
    private EntityManager entityManager;

    @Override
    @RolesAllowed("admin")
    public void register(String username, String password, int userTypeId) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserType(UserType.getUserType(userTypeId));
        entityManager.persist(user);
    }
}
