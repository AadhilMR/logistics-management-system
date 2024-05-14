package com.aadhil.ejb.remote;

import java.util.List;

import com.aadhil.ejb.dto.CargoDTO;
import com.aadhil.ejb.dto.RouteDTO;
import com.aadhil.ejb.entity.Cargo;
import com.aadhil.ejb.entity.Terminal;
import jakarta.ejb.Remote;

@Remote
public interface DataFetchService {
    List<Terminal> fetchTerminals();
    List<Terminal> fetchTerminals(List<String> terminalNames);
    List<RouteDTO> fetchRoutes();
    List<CargoDTO> fetchCargoAsDTO();
    Cargo fetchLastInsertCargo();
}
