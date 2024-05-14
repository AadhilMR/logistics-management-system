package com.aadhil.ejb.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.aadhil.ejb.entity.Route;
import com.aadhil.ejb.entity.RouteHasTerminal;
import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.exception.RouteCreationException;
import com.aadhil.ejb.interceptor.CalculateTimeInterceptor;
import com.aadhil.ejb.interceptor.FetchTerminalsInterceptor;
import com.aadhil.ejb.remote.RouteService;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class RouteServiceBean implements RouteService {

    @PersistenceContext(unitName = "LMS_PU")
    private EntityManager em;

    @Inject
    private UserTransaction transaction;

    private List<Terminal> terminalList;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    @Override
    @Interceptors({FetchTerminalsInterceptor.class, CalculateTimeInterceptor.class})
    public Long createRoute(String name, String terminals) {
        try {
            if(name.isEmpty() || terminals.isEmpty()) {
                throw new RouteCreationException("Required parameters are empty");
            }

            transaction.begin();

            // Insert new route
            Route route = new Route();
            route.setName(name);
            route.setDepartureTime(departureTime);
            route.setArrivalTime(arrivalTime);
            em.persist(route);
            em.flush();

            for(int i=0; i<terminalList.size(); i++) {
                long order = i+1;
                RouteHasTerminal routeHasTerminal = new RouteHasTerminal();
                routeHasTerminal.setRoute(route);
                routeHasTerminal.setTerminal(terminalList.get(i));
                routeHasTerminal.setOrder(order);
                em.persist(routeHasTerminal);
            }

            transaction.commit();

            return ChronoUnit.DAYS.between(departureTime, arrivalTime);
        } catch (Exception ex) {
            try {
                transaction.rollback();
            } catch (SystemException ignored) {

            } finally {
                throw new RouteCreationException("Route creation failed");
            }
        }
    }

    public List<Terminal> getTerminalList() {
        return terminalList;
    }

    public void setTerminalList(List<Terminal> terminalList) {
        this.terminalList = terminalList;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
