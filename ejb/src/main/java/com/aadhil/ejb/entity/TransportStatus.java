package com.aadhil.ejb.entity;

import java.io.Serializable;

public enum TransportStatus implements Serializable {
    IN_TRANSIT, IN_PORT, UNKNOWN, CLAIMED, NOT_RECEIVED;

    public boolean isSame(TransportStatus status) {
        return this == status;
    }
}