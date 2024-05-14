package com.aadhil.ejb.dto;

import java.util.List;

public class RouteDTO {
    private Long id;
    private String name;
    private String nextDepartureTime;
    private int daysForVoyage;
    private List<String> terminalList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextDepartureTime() {
        return nextDepartureTime;
    }

    public void setNextDepartureTime(String nextDepartureTime) {
        this.nextDepartureTime = nextDepartureTime;
    }

    public int getDaysForVoyage() {
        return daysForVoyage;
    }

    public void setDaysForVoyage(int daysForVoyage) {
        this.daysForVoyage = daysForVoyage;
    }

    public List<String> getTerminalList() {
        return terminalList;
    }

    public void setTerminalList(List<String> terminalList) {
        this.terminalList = terminalList;
    }
}
