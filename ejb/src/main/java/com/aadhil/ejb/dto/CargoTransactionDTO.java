package com.aadhil.ejb.dto;

public class CargoTransactionDTO {
    private String trackingId;
    private String cargoId;
    private String cargoDescription;
    private String originName;
    private String originCode;
    private String destinationName;
    private String destinationCode;
    private String lastLocationName;
    private String lastLocationCode;
    private String transportStatus;
    private String departureDateTime;
    private String arrivalDateTime;

    public String getTrackingId() {
        return trackingId;
    }

    private void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getCargoId() {
        return cargoId;
    }

    private void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    private void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public String getOriginName() {
        return originName;
    }

    private void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginCode() {
        return originCode;
    }

    private void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDestinationName() {
        return destinationName;
    }

    private void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    private void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public String getLastLocationName() {
        return lastLocationName;
    }

    private void setLastLocationName(String lastLocationName) {
        this.lastLocationName = lastLocationName;
    }

    public String getLastLocationCode() {
        return lastLocationCode;
    }

    private void setLastLocationCode(String lastLocationCode) {
        this.lastLocationCode = lastLocationCode;
    }

    public String getTransportStatus() {
        return transportStatus;
    }

    private void setTransportStatus(String transportStatus) {
        this.transportStatus = transportStatus;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    private void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    private void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public static class Builder {
        private String trackingId;
        private String cargoId;
        private String cargoDescription;
        private String originName;
        private String originCode;
        private String destinationName;
        private String destinationCode;
        private String lastLocationName;
        private String lastLocationCode;
        private String transportStatus;
        private String departureDateTime;
        private String arrivalDateTime;

        public CargoTransactionDTO.Builder setTrackingId(String trackingId) {
            this.trackingId = trackingId;
            return this;
        }

        public CargoTransactionDTO.Builder setCargoId(String cargoId) {
            this.cargoId = cargoId;
            return this;
        }

        public CargoTransactionDTO.Builder setCargoDescription(String cargoDescription) {
            this.cargoDescription = cargoDescription;
            return this;
        }

        public CargoTransactionDTO.Builder setOriginName(String originName) {
            this.originName = originName;
            return this;
        }

        public CargoTransactionDTO.Builder setOriginCode(String originCode) {
            this.originCode = originCode;
            return this;
        }

        public CargoTransactionDTO.Builder setDestinationName(String destinationName) {
            this.destinationName = destinationName;
            return this;
        }

        public CargoTransactionDTO.Builder setDestinationCode(String destinationCode) {
            this.destinationCode = destinationCode;
            return this;
        }

        public CargoTransactionDTO.Builder setLastLocationName(String lastLocationName) {
            this.lastLocationName = lastLocationName;
            return this;
        }

        public CargoTransactionDTO.Builder setLastLocationCode(String lastLocationCode) {
            this.lastLocationCode = lastLocationCode;
            return this;
        }

        public CargoTransactionDTO.Builder setTransportStatus(String transportStatus) {
            this.transportStatus = transportStatus;
            return this;
        }

        public CargoTransactionDTO.Builder setDepartureDateTime(String departureDateTime) {
            this.departureDateTime = departureDateTime;
            return this;
        }

        public CargoTransactionDTO.Builder setArrivalDateTime(String arrivalDateTime) {
            this.arrivalDateTime = arrivalDateTime;
            return this;
        }

        public CargoTransactionDTO build() {
            CargoTransactionDTO cargoTransactionDTO = new CargoTransactionDTO();
            cargoTransactionDTO.setTrackingId(trackingId);
            cargoTransactionDTO.setCargoId(cargoId);
            cargoTransactionDTO.setCargoDescription(cargoDescription);
            cargoTransactionDTO.setOriginName(originName);
            cargoTransactionDTO.setOriginCode(originCode);
            cargoTransactionDTO.setDestinationName(destinationName);
            cargoTransactionDTO.setDestinationCode(destinationCode);
            cargoTransactionDTO.setLastLocationName(lastLocationName);
            cargoTransactionDTO.setLastLocationCode(lastLocationCode);
            cargoTransactionDTO.setTransportStatus(transportStatus);
            cargoTransactionDTO.setDepartureDateTime(departureDateTime);
            cargoTransactionDTO.setArrivalDateTime(arrivalDateTime);

            return cargoTransactionDTO;
        }
    } 
}
