package com.aadhil.ejb.impl;

import java.util.HashMap;

import com.aadhil.ejb.entity.Cargo;
import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.entity.TransportStatus;
import com.aadhil.ejb.entity.CargoTransaction;
import com.aadhil.ejb.exception.CargoTransactionException;
import com.aadhil.ejb.interceptor.RouteDeterminationInterceptor;
import com.aadhil.ejb.interceptor.TransactionParameterValidationInterceptor;
import com.aadhil.ejb.remote.CargoTransactionService;
import com.aadhil.ejb.remote.DataFetchService;
import com.aadhil.ejb.util.TrackingIDGenerator;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CargoTransactionServiceBean implements CargoTransactionService {
    private final CargoTransaction.Builder builder = new CargoTransaction.Builder();

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private DataFetchService dataFetchService;

    @Override
    @Interceptors({TransactionParameterValidationInterceptor.class, RouteDeterminationInterceptor.class})
    public HashMap<String, String> createTransaction(String cargoId, int originId, int destinationId) {

        Cargo cargo = dataFetchService.fetchCargo(cargoId);
        Terminal origin = dataFetchService.fetchTerminal(originId);
        Terminal destination = dataFetchService.fetchTerminal(destinationId);
        Terminal lastLocation = origin; // At first, the cargo's last known location is same to origin
        TransportStatus status = TransportStatus.IN_PORT;

        TrackingIDGenerator idGenerator = new TrackingIDGenerator();
        String trackingId = idGenerator.generateTrackingId();

        builder.setCargo(cargo);
        builder.setTrackingId(trackingId);
        builder.setOrigin(origin);
        builder.setDestination(destination);
        builder.setLastLocation(lastLocation);
        builder.setStatus(status);
        CargoTransaction cargoTransaction = builder.build();

        try {
            entityManager.persist(cargoTransaction);

            HashMap<String, String> responseData = new HashMap<>();
            responseData.put("trackingId", trackingId);
            responseData.put("route", cargoTransaction.getRoute().getName());

            return responseData;
        } catch (Exception ex) {
            throw new CargoTransactionException("Failed to set route.");
        }
    }

    public CargoTransaction.Builder getBuilder() {
        return builder;
    }
}
