package com.aadhil.ejb.interceptor;

import java.time.LocalDateTime;
import java.util.List;

import com.aadhil.ejb.entity.CargoTransaction;
import com.aadhil.ejb.entity.Route;
import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.exception.CargoTransactionException;
import com.aadhil.ejb.impl.CargoTransactionServiceBean;
import com.aadhil.ejb.util.NauticalCalculator;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class RouteDeterminationInterceptor {

    @PersistenceContext
    private EntityManager entityManager;

    private final NauticalCalculator calculator = new NauticalCalculator();
    private Route selectedRoute = null;
    private LocalDateTime departureTime = null;
    private LocalDateTime arrivalTime = null;

    @AroundInvoke
    public Object determineRoute(InvocationContext invocationContext) throws Exception {
        CargoTransactionServiceBean transactionServiceBean = (CargoTransactionServiceBean) invocationContext.getTarget();
        CargoTransaction.Builder builder = transactionServiceBean.getBuilder();

        Object[] parameters = invocationContext.getParameters();
        int originId = (int) parameters[1];
        int destinationId = (int) parameters[2];

        setData(originId, destinationId);

        builder.setRoute(selectedRoute);
        builder.setDepartureTime(departureTime);
        builder.setArrivalTime(arrivalTime);

        return invocationContext.proceed();
    }

    private void setData(int originId, int destinationId) {
        List<Route> routeList = getRouteList(originId, destinationId);
        int daysToOrigin = 0;
        int daysToDestination = 0;

        for(Route route : routeList) {
            List<Terminal> terminalList = getTerminalList(route.getId());

            int daysToCurrentOrigin = calculateDaysToOrigin(terminalList, originId);
            int daysToCurrentDestination = calculateDaysToDestination(terminalList, destinationId);

            if(daysToDestination == 0) {
                selectedRoute = route;
                daysToOrigin = daysToCurrentOrigin;
                daysToDestination = daysToCurrentDestination;
            } else if(daysToCurrentDestination < daysToDestination) {
                selectedRoute = route;
                daysToOrigin = daysToCurrentOrigin;
                daysToDestination = daysToCurrentDestination;
            }
        }

        departureTime = calculator.getArrivalTime(selectedRoute.getDepartureTime(), daysToOrigin);
        arrivalTime = calculator.getArrivalTime(selectedRoute.getDepartureTime(), daysToDestination);
    }

    private List<Route> getRouteList(int originId, int destinationId) {
        List<Route> routeList = entityManager.createQuery("SELECT DISTINCT r FROM Route r " +
                        "WHERE EXISTS (" +
                        "SELECT 1 FROM RouteHasTerminal rht1 " +
                        "JOIN RouteHasTerminal rht2 ON rht1.route = r AND rht2.route = r " +
                        "WHERE rht1.terminal.id = :terminalId1 " +
                        "AND rht2.terminal.id = :terminalId2 " +
                        "AND rht1.order < rht2.order)", Route.class)
                .setParameter("terminalId1", originId)
                .setParameter("terminalId2", destinationId)
                .getResultList();

        if(routeList.isEmpty()) {
            throw new CargoTransactionException("No routes matched.");
        }
        return routeList;
    }

    private List<Terminal> getTerminalList(Long routeId) {
        return entityManager.createQuery(
                        "SELECT rht.terminal FROM RouteHasTerminal rht WHERE rht.route.id = :routeId ORDER BY rht.order", Terminal.class)
                .setParameter("routeId", routeId)
                .getResultList();
    }

    private int calculateDaysToOrigin(List<Terminal> terminalList, int originId) {
        Terminal routeOrigin = terminalList.get(0);
        double distanceToOrigin = 0.0;
        int numberOfTerminals = 0;

        for (Terminal terminal : terminalList) {
            distanceToOrigin += calculator
                    .calculateDistance(routeOrigin.getLatitude(), routeOrigin.getLongitude(),
                            terminal.getLatitude(), terminal.getLongitude());

            if(originId == terminal.getId()) {
                break;
            }

            numberOfTerminals++;
        }

        return calculator.calculateDuration(distanceToOrigin) + numberOfTerminals;
    }

    private int calculateDaysToDestination(List<Terminal> terminalList, int destinationId) {
        Terminal routeOrigin = terminalList.get(0);
        double distanceToDestination = 0.0;
        int numberOfTerminals = 0;

        for (Terminal terminal : terminalList) {
            distanceToDestination += calculator
                    .calculateDistance(routeOrigin.getLatitude(), routeOrigin.getLongitude(),
                            terminal.getLatitude(), terminal.getLongitude());

            if(destinationId == terminal.getId()) {
                break;
            }

            numberOfTerminals++;
        }

        return calculator.calculateDuration(distanceToDestination) + numberOfTerminals;
    }
}
