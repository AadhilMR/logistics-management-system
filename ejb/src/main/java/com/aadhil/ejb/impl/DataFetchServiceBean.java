package com.aadhil.ejb.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.aadhil.ejb.dto.CargoDTO;
import com.aadhil.ejb.dto.CargoTransactionDTO;
import com.aadhil.ejb.dto.RouteDTO;
import com.aadhil.ejb.entity.Cargo;
import com.aadhil.ejb.entity.Terminal;
import com.aadhil.ejb.entity.TransportStatus;
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
    public RouteDTO fetchRoute(Long id) {
        for (RouteDTO routeDTO : fetchRoutes()) {
            if(Objects.equals(routeDTO.getId(), id)) {
                return routeDTO;
            }
        }

        return null;
    }

    @Override
    public Cargo fetchCargo(String cargoId) {
        return entityManager.createQuery("SELECT c FROM Cargo c WHERE c.cargoId = :cargoId", Cargo.class)
                .setParameter("cargoId", cargoId)
                .getSingleResult();
    }

    @Override
    public List<CargoTransactionDTO> fetchTransactions() {
        List<Object[]> results = entityManager.createQuery(
                        "SELECT ct.trackingId, " +
                                "c.cargoId, " +
                                "c.description, " +
                                "o.name, " +
                                "o.code, " +
                                "d.name, " +
                                "d.code, " +
                                "l.name, " +
                                "l.code, " +
                                "ct.status, " +
                                "ct.departureTime, " +
                                "ct.arrivalTime " +
                                "FROM CargoTransaction ct " +
                                "JOIN ct.cargo c " +
                                "JOIN ct.origin o " +
                                "JOIN ct.destination d " +
                                "JOIN ct.lastLocation l",
                        Object[].class)
                .getResultList();

        List<CargoTransactionDTO> cargoTransactionDTOList = new ArrayList<>();

        for (Object[] result : results) {
            CargoTransactionDTO.Builder builder = new CargoTransactionDTO.Builder();

            String pattern = "yyyy-MM-dd HH:mm";
            String departureTime = getNextDepartureTime((LocalDateTime) result[10], pattern);
            String arrivalTime = getNextArrivalTime((LocalDateTime) result[11], (LocalDateTime) result[10], pattern);

            builder.setTrackingId((String) result[0])
                    .setCargoId((String) result[1])
                    .setCargoDescription((String) result[2])
                    .setOriginName((String) result[3])
                    .setOriginCode((String) result[4])
                    .setDestinationName((String) result[5])
                    .setDestinationCode((String) result[6])
                    .setLastLocationName((String) result[7])
                    .setLastLocationCode((String) result[8])
                    .setTransportStatus(((TransportStatus) result[9]).toString())
                    .setDepartureDateTime(departureTime)
                    .setArrivalDateTime(arrivalTime);

            cargoTransactionDTOList.add(builder.build());
        }
        return cargoTransactionDTOList;
    }

    @Override
    public CargoTransactionDTO fetchTransaction(String trackingId) {
        Object[] result = entityManager.createQuery(
                "SELECT ct.trackingId, " +
                        "c.cargoId, " +
                        "c.description, " +
                        "o.name, " +
                        "o.code, " +
                        "d.name, " +
                        "d.code, " +
                        "l.name, " +
                        "l.code, " +
                        "ct.status, " +
                        "ct.departureTime, " +
                        "ct.arrivalTime " +
                        "FROM CargoTransaction ct " +
                        "JOIN ct.cargo c " +
                        "JOIN ct.origin o " +
                        "JOIN ct.destination d " +
                        "JOIN ct.lastLocation l " +
                        "WHERE ct.trackingId=:trackingId",
                Object[].class)
                .setParameter("trackingId", trackingId)
                .getSingleResult();

        CargoTransactionDTO.Builder builder = new CargoTransactionDTO.Builder();

        String pattern = "yyyy-MM-dd HH:mm";
        String departureTime = getNextDepartureTime((LocalDateTime) result[10], pattern);
        String arrivalTime = getNextArrivalTime((LocalDateTime) result[11], (LocalDateTime) result[10], pattern);

        builder.setTrackingId((String) result[0])
                .setCargoId((String) result[1])
                .setCargoDescription((String) result[2])
                .setOriginName((String) result[3])
                .setOriginCode((String) result[4])
                .setDestinationName((String) result[5])
                .setDestinationCode((String) result[6])
                .setLastLocationName((String) result[7])
                .setLastLocationCode((String) result[8])
                .setTransportStatus(((TransportStatus) result[9]).toString())
                .setDepartureDateTime(departureTime)
                .setArrivalDateTime(arrivalTime);

        return builder.build();
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

    private String getNextDepartureTime(LocalDateTime departureTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        departureTime = getNextDepartureTime(departureTime);

        return departureTime.format(formatter);
    }

    private String getNextArrivalTime(LocalDateTime arrivalTime, LocalDateTime departureTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        departureTime = getNextDepartureTime(departureTime);
        arrivalTime = arrivalTime.withMonth(departureTime.getMonthValue());

        return arrivalTime.format(formatter);
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
                routeDTO.setNextDepartureTime(getNextDepartureTime(departureTime, "MM-dd HH:mm"));
                routeDTO.setTerminalList(terminalList);

                routeDTOHashMap.put(routeName, routeDTO);
            }
        }

        return routeDTOHashMap;
    }
}
