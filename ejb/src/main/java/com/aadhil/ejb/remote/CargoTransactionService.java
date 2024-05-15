package com.aadhil.ejb.remote;

import java.util.HashMap;

import jakarta.ejb.Remote;

@Remote
public interface CargoTransactionService {
    HashMap<String, String> createTransaction(String cargoId, int originId, int destinationId);
}