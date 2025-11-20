package org.gruppe4.graphql.query;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class QueryObject {
    private int fromStop;
    private int toStop;
    private Integer viaStop = null;
    public OffsetDateTime dateTime;
    private String transportMode;
    private ArrayList<String> transportModes;

    public QueryObject(int fromStop, int toStop) {
        this.fromStop = fromStop;
        this.toStop = toStop;
    }

    public QueryObject(int fromStop, int toStop, OffsetDateTime dateTime) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.dateTime = dateTime;
    }

    public QueryObject(int fromStop, int toStop, String transportMode) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.transportMode = transportMode;
    }

    public QueryObject(int fromStop, int toStop, OffsetDateTime dateTime, String transportMode) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.dateTime = dateTime;
        this.transportMode = transportMode;
    }

    public QueryObject(int fromStop, int toStop, ArrayList<String> transportModes) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.transportModes = transportModes;
    }

    public QueryObject(int fromStop, int toStop, OffsetDateTime dateTime, ArrayList<String> transportModes) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.dateTime = dateTime;
        this.transportModes = new ArrayList<String>(transportModes);
    }

    public QueryObject(int fromStop, int toStop, int viaStop) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.viaStop = viaStop;
    }

    public QueryObject(int fromStop, int toStop, int viaStop, OffsetDateTime dateTime) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.viaStop = viaStop;
    }

    public QueryObject(int fromStop, int toStop, int viaStop, String transportMode) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.viaStop = viaStop;
        this.transportMode = transportMode;
    }

    public QueryObject(int fromStop, int toStop, int viaStop, OffsetDateTime dateTime, String transportMode) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.viaStop = viaStop;
        this.dateTime = dateTime;
        this.transportMode = transportMode;
    }

    public QueryObject(int fromStop, int toStop, int viaStop, ArrayList<String> transportModes) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.viaStop = viaStop;
        this.transportModes = transportModes;
    }

    public QueryObject(int fromStop, int toStop, int viaStop, OffsetDateTime dateTime, ArrayList<String> transportModes) {
        this.fromStop = fromStop;
        this.toStop = toStop;
        this.viaStop = viaStop;
        this.dateTime = dateTime;
        this.transportModes = transportModes;
    }

    public int getFromStop() {
        return fromStop;
    }

    public int getToStop() {
        return toStop;
    }

    public Integer getViaStop() {
        return viaStop;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public ArrayList<String> getTransportModes() {
        return transportModes;
    }
}
