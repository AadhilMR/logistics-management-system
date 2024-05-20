package com.aadhil.ejb.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.aadhil.ejb.dto.CargoTrackerDTO;
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
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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
    @RolesAllowed({"admin", "supervisor"})
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
    @RolesAllowed({"admin", "supervisor"})
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

    @Override
    @PermitAll
    public CargoTrackerDTO trackTransaction(String trackingId) {
        CargoTransaction cargoTransaction = entityManager.createQuery("SELECT ct FROM CargoTransaction ct WHERE ct.trackingId = :trackingId", CargoTransaction.class)
                .setParameter("trackingId", trackingId)
                .getSingleResult();

        Terminal origin = cargoTransaction.getOrigin();
        Terminal destination = cargoTransaction.getDestination();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);
        LocalDateTime deparuteDateTime = getNextDepartureTime(cargoTransaction.getDepartureTime());
        LocalDateTime arrivalDateTime = cargoTransaction.getArrivalTime().withMonth(deparuteDateTime.getMonthValue());

        CargoTrackerDTO cargoTrackerDTO = new CargoTrackerDTO();
        cargoTrackerDTO.setStatus(cargoTransaction.getStatus().toString());
        cargoTrackerDTO.setLastLocation(cargoTransaction.getLastLocation().getName());
        cargoTrackerDTO.setArrivalTime(formatter.format(arrivalDateTime));
        cargoTrackerDTO.setDepartureTime(formatter.format(deparuteDateTime));
        cargoTrackerDTO.setTerminals(filterTerminalList(cargoTransaction.getRoute().getId(), origin, destination));

        return cargoTrackerDTO;
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

    private List<Terminal> getTerminalList(Long routeId) {
        return entityManager.createQuery(
                        "SELECT rht.terminal FROM RouteHasTerminal rht WHERE rht.route.id = :routeId ORDER BY rht.order", Terminal.class)
                .setParameter("routeId", routeId)
                .getResultList();
    }

    private List<Terminal> filterTerminalList(Long routeId, Terminal origin, Terminal destination) {
        List<Terminal> terminalList = getTerminalList(routeId);

        boolean canRemove = true;

        for (int i=0; i<terminalList.size(); i++) {
            Terminal terminal = terminalList.get(i);

            if(terminal.equals(origin)) {
                canRemove = false;
            }

            if(canRemove) {
                terminalList.remove(terminal);
                i--;
            }

            if(terminal.equals(destination)) {
                canRemove = true;
            }
        }

        return terminalList;
    }

    private LocalDateTime getNextDepartureTime(LocalDateTime departureTime) {
        int currentMonth = LocalDateTime.now().getMonthValue();

        departureTime = departureTime.withMonth(currentMonth);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (departureTime.isBefore(currentDateTime)) {
            departureTime = departureTime.plusMonths(1);
        }
        return departureTime;
    }
}
