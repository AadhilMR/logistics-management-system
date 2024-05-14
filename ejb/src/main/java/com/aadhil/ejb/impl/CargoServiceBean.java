package com.aadhil.ejb.impl;

import java.time.LocalDateTime;

import com.aadhil.ejb.entity.Cargo;
import com.aadhil.ejb.exception.CargoCreationFailedException;
import com.aadhil.ejb.interceptor.CargoIdGenerationInterceptor;
import com.aadhil.ejb.remote.CargoService;
import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CargoServiceBean implements CargoService {

    @PersistenceContext
    private EntityManager entityManager;

    private String cargoId;

    @Override
    @Interceptors(CargoIdGenerationInterceptor.class)
    public void createCargo(String description, String instruction) {
        try {
            LocalDateTime createdDateTime = LocalDateTime.now();

            Cargo cargo = new Cargo();
            cargo.setCargoId(cargoId);
            cargo.setDescription(description);
            cargo.setInstruction(instruction);
            cargo.setCreatedTime(createdDateTime);

            entityManager.persist(cargo);
        } catch (Exception e) {
            throw new CargoCreationFailedException("Cargo creation failed");
        }
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }
}
