package com.aadhil.ejb.dto;

public class CargoDTO {
    private String cargoId;
    private String cargoDescription;
    private String cargoInstruction;
    private String createdDate;
    private String cargoStatus;

    public String getCargoId() {
        return cargoId;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public String getCargoInstruction() {
        return cargoInstruction;
    }

    public void setCargoInstruction(String cargoInstruction) {
        this.cargoInstruction = cargoInstruction;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCargoStatus() {
        return cargoStatus;
    }

    public void setCargoStatus(String cargoStatus) {
        this.cargoStatus = cargoStatus;
    }
}