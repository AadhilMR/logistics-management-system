package com.aadhil.ejb.remote;

import jakarta.ejb.Remote;

@Remote
public interface CargoService {
    void createCargo(String description, String instruction);
}
