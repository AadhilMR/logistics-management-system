package com.aadhil.ejb.impl;

import com.aadhil.ejb.entity.User;
import com.aadhil.ejb.remote.LoginService;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class LoginServiceBean implements LoginService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User login(String username, String password) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username AND u.password=:password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }
}
