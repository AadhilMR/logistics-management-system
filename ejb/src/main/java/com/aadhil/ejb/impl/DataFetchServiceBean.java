package com.aadhil.ejb.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aadhil.ejb.dto.RouteDTO;
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

    @Override
    public List<Terminal> fetchTerminals(List<String> terminalNames) {
        return entityManager.createQuery("SELECT t FROM Terminal t WHERE t.name IN :names", Terminal.class)
                .setParameter("names", terminalNames)
                .getResultList();
    }

    @Override
    public List<RouteDTO> fetchRoutes() {
        // Get Route List
        String nativeQuery = "SELECT r.id, r.arrival_time, r.departure_time, r.name, t.name " +
                "FROM `route` r " +
                "INNER JOIN route_has_terminal rht ON rht.route_id=r.id " +
                "INNER JOIN terminal t ON t.id=rht.terminal_id " +
                "ORDER BY rht.term_order ASC;";

        @SuppressWarnings("unchecked")
        List<Object[]> routeDataList = entityManager.createNativeQuery(nativeQuery).getResultList();

        List<RouteDTO> routeDTOList = new ArrayList<>();

        for (Map.Entry<String, RouteDTO> entry : getRouteDTOHashMap(routeDataList).entrySet()) {
            routeDTOList.add(entry.getValue());
        }

        return routeDTOList;
    }

    private String getNextDepartureTime(LocalDateTime departureTime) {
        int currentMonth = LocalDateTime.now().getMonthValue();

        departureTime = departureTime.withMonth(currentMonth);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (departureTime.isBefore(currentDateTime)) {
            departureTime = departureTime.plusMonths(1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        return departureTime.format(formatter);
    }

    private HashMap<String, RouteDTO> getRouteDTOHashMap(List<Object[]> routeDataList) {
        HashMap<String, RouteDTO> routeDTOHashMap = new HashMap<>();

        // Convert data into RouteDTO objects
        for(Object[] rows : routeDataList) {
            Long id = (Long) rows[0];
            LocalDateTime arrivalTime = (LocalDateTime) rows[1];
            LocalDateTime departureTime = (LocalDateTime) rows[2];
            String routeName = (String) rows[3];
            String terminalName = (String) rows[4];

            if(routeDTOHashMap.containsKey(routeName)) {
                List<String> terminalList = routeDTOHashMap.get(routeName).getTerminalList();
                terminalList.add(terminalName);
            } else {
                List<String> terminalList = new ArrayList<>();
                terminalList.add(terminalName);

                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setId(id);
                routeDTO.setName(routeName);
                routeDTO.setDaysForVoyage((int) ChronoUnit.DAYS.between(departureTime, arrivalTime));
                routeDTO.setNextDepartureTime(getNextDepartureTime(departureTime));
                routeDTO.setTerminalList(terminalList);

                routeDTOHashMap.put(routeName, routeDTO);
            }
        }

        return routeDTOHashMap;
    }
}
