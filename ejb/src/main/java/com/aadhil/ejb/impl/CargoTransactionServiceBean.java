package com.aadhil.ejb.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aadhil.ejb.dto.RouteDTO;
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

    @Override
    public void updateStatus(String trackingId, String status) {
        CargoTransaction cargoTransaction = entityManager
                .createQuery("SELECT ct FROM CargoTransaction ct WHERE ct.trackingId = :trackingId",
                        CargoTransaction.class)
                .setParameter("trackingId", trackingId)
                .getSingleResult();

        if(cargoTransaction == null) {
            throw new CargoTransactionException("Row is not found.");
        } else {
            TransportStatus transportStatus = getTransportStatus(status);

            if(transportStatus.isSame(TransportStatus.IN_PORT) || transportStatus.isSame(TransportStatus.CLAIMED)) {
                cargoTransaction.setLastLocation(getNextTerminal(cargoTransaction));
            } else if(transportStatus.isSame(TransportStatus.NOT_RECEIVED)) {
                if(cargoTransaction.getArrivalTime().isAfter(LocalDateTime.now())) {
                    return;
                }
            }

            cargoTransaction.setStatus(transportStatus);
            entityManager.merge(cargoTransaction);
        }
    }

    private Terminal getNextTerminal(CargoTransaction cargoTransaction) {
        RouteDTO routeDTO = dataFetchService.fetchRoute(cargoTransaction.getRoute().getId());

        for(int i=0; i<routeDTO.getTerminalList().size(); i++) {
            String terminal = routeDTO.getTerminalList().get(i);

            if(terminal.equals(cargoTransaction.getLastLocation().getName())) {
                List<String> terminalList = new ArrayList<>();
                terminalList.add(routeDTO.getTerminalList().get(i+1));

                return dataFetchService.fetchTerminals(terminalList).get(0);
            }
        }
        return null;
    }

    private TransportStatus getTransportStatus(String status) {
        TransportStatus transportStatus;

        switch (status) {
            case "IN_TRANSIT":
                transportStatus = TransportStatus.IN_TRANSIT;
                break;
            case "IN_PORT":
                transportStatus = TransportStatus.IN_PORT;
                break;
            case "NOT_RECEIVED":
                transportStatus = TransportStatus.NOT_RECEIVED;
                break;
            case "CLAIMED":
                transportStatus = TransportStatus.CLAIMED;
                break;
            default:
                transportStatus = TransportStatus.UNKNOWN;
        }
        return transportStatus;
    }
}
