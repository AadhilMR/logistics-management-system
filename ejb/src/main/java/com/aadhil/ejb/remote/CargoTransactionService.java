package com.aadhil.ejb.remote;

import java.util.HashMap;

import com.aadhil.ejb.dto.CargoTrackerDTO;
import jakarta.ejb.Remote;

@Remote
public interface CargoTransactionService {
    HashMap<String, String> createTransaction(String cargoId, int originId, int destinationId);
    void updateStatus(String trackingId, String status);
    CargoTrackerDTO trackTransaction(String trackingId);
}