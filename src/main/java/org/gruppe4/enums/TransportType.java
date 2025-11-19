package org.gruppe4.enums;

public enum TransportType {
    BUS("bus"),
    RAIL("rail"),
    METRO("metro"),
    TRAM("tram"),
    COACH("coach"),
    FERRY("ferry");

    private final String type;

    TransportType(String type) {
        this.type = type;
    }

    public String getMode() {
        return type;
    }
}