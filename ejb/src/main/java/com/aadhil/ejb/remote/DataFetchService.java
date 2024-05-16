package com.aadhil.ejb.remote;

import java.util.List;

import com.aadhil.ejb.dto.CargoDTO;
import com.aadhil.ejb.dto.CargoTransactionDTO;
import com.aadhil.ejb.dto.RouteDTO;
import com.aadhil.ejb.entity.Cargo;
import com.aadhil.ejb.entity.Terminal;
import jakarta.ejb.Remote;

@Remote
public interface DataFetchService {
    List<Terminal> fetchTerminals();
    List<Terminal> fetchTerminals(List<String> terminalNames);
    Terminal fetchTerminal(int id);
    List<RouteDTO> fetchRoutes();
    List<CargoDTO> fetchCargoAsDTO();
    Cargo fetchLastInsertCargo();
    Cargo fetchCargo(String cargoId);
    List<CargoTransactionDTO> fetchTransactions();
    CargoTransactionDTO fetchTransaction(String trackingId);
}
