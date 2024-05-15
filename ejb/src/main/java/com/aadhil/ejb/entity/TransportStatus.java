package com.aadhil.ejb.entity;

public enum TransportStatus {
    IN_TRANSIT, IN_PORT, UNKNOWN, CLAIMED, NOT_RECEIVED;

    public boolean isSame(TransportStatus status) {
        return this == status;
    }
}