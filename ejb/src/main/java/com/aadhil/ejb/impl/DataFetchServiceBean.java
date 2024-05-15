package com.aadhil.ejb.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aadhil.ejb.dto.CargoDTO;
import com.aadhil.ejb.dto.RouteDTO;
import com.aadhil.ejb.entity.Cargo;
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
        List<Terminal> terminalList = new ArrayList<>();

        for (String terminalName : terminalNames) {
            Terminal terminal = entityManager.createQuery("SELECT t FROM Terminal t WHERE t.name=:name", Terminal.class)
                    .setParameter("name", terminalName)
                    .getSingleResult();

            terminalList.add(terminal);
        }

        return terminalList;
    }

    @Override
    public Terminal fetchTerminal(int id) {
        return entityManager.createQuery("SELECT t FROM Terminal t WHERE t.id = :id", Terminal.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<CargoDTO> fetchCargoAsDTO() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<CargoDTO> cargoDTOList = new ArrayList<>();

        List<Cargo> cargoList = entityManager.createQuery("SELECT c FROM Cargo c", Cargo.class).getResultList();

        for(Cargo cargo : cargoList) {
            // Check the status of the Cargo
            Long resultCount = (Long) entityManager.createQuery("SELECT COUNT(ct.id) FROM CargoTransaction ct WHERE ct.cargo.id = :cargoId")
                    .setParameter("cargoId", cargo.getId())
                    .getSingleResult();

            CargoDTO cargoDTO = new CargoDTO();

            cargoDTO.setCargoId(cargo.getCargoId());
            cargoDTO.setCargoDescription(cargo.getDescription());
            cargoDTO.setCargoInstruction(cargo.getInstruction());
            cargoDTO.setCreatedDate(cargo.getCreatedTime().format(formatter));

            if(resultCount == 1) {
                cargoDTO.setCargoStatus("route-specified");
            } else {
                cargoDTO.setCargoStatus("not-specified");
            }

            cargoDTOList.add(cargoDTO);
        }

        return cargoDTOList;
    }

    @Override
    public Cargo fetchLastInsertCargo() {
        try {
            return entityManager.createQuery("SELECT c FROM Cargo c ORDER BY c.id DESC", Cargo.class)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
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

    @Override
    public Cargo fetchCargo(String cargoId) {
        return entityManager.createQuery("SELECT c FROM Cargo c WHERE c.cargoId = :cargoId", Cargo.class)
                .setParameter("cargoId", cargoId)
                .getSingleResult();
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
