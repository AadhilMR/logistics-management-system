package com.aadhil.ejb.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "cargo_transaction")
public class CargoTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;
    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
    @Column(name = "tracking_id", nullable = false)
    private String trackingId;
    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;
    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private Terminal origin;
    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private Terminal destination;
    @ManyToOne
    @JoinColumn(name = "last_location_id", nullable = false)
    private Terminal lastLocation;
    @Column(name = "status", nullable = false)
    private TransportStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Terminal getOrigin() {
        return origin;
    }

    public void setOrigin(Terminal origin) {
        this.origin = origin;
    }

    public Terminal getDestination() {
        return destination;
    }

    public void setDestination(Terminal destination) {
        this.destination = destination;
    }

    public Terminal getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Terminal lastLocation) {
        this.lastLocation = lastLocation;
    }

    public TransportStatus getStatus() {
        return status;
    }

    public void setStatus(TransportStatus status) {
        this.status = status;
    }

    public static class Builder {
        private Long id;
        private Cargo cargo;
        private Route route;
        private String trackingId;
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;
        private Terminal origin;
        private Terminal destination;
        private Terminal lastLocation;
        private TransportStatus status;

        public void setId(Long id) {
            this.id = id;
        }

        public void setCargo(Cargo cargo) {
            this.cargo = cargo;
        }

        public void setRoute(Route route) {
            this.route = route;
        }

        public void setTrackingId(String trackingId) {
            this.trackingId = trackingId;
        }

        public void setDepartureTime(LocalDateTime departureTime) {
            this.departureTime = departureTime;
        }

        public void setArrivalTime(LocalDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public void setOrigin(Terminal origin) {
            this.origin = origin;
        }

        public void setDestination(Terminal destination) {
            this.destination = destination;
        }

        public void setLastLocation(Terminal lastLocation) {
            this.lastLocation = lastLocation;
        }

        public void setStatus(TransportStatus status) {
            this.status = status;
        }

        public CargoTransaction build() {
            CargoTransaction cargoTransaction = new CargoTransaction();
            cargoTransaction.setCargo(cargo);
            cargoTransaction.setRoute(route);
            cargoTransaction.setTrackingId(trackingId);
            cargoTransaction.setDepartureTime(departureTime);
            cargoTransaction.setArrivalTime(arrivalTime);
            cargoTransaction.setOrigin(origin);
            cargoTransaction.setDestination(destination);
            cargoTransaction.setLastLocation(lastLocation);
            cargoTransaction.setStatus(status);

            return cargoTransaction;
        }
    }
}