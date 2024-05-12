package com.aadhil.ejb.impl;

import java.util.List;

import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.remote.DataFetchService;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class DataFetchServiceBean implements DataFetchService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Terminal> fetchTerminals() {
        return entityManager.createQuery("SELECT t FROM Terminal t", Terminal.class)
                .getResultList();
    }
}
