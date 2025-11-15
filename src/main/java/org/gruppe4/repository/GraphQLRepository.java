package org.gruppe4.repository;
import graphql.Service.EnturResponseService;
import graphql.client.GraphQLClient;
import graphql.dto.*;
import graphql.query.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;

public class GraphQLRepository {
    private final GraphQLClient client;

    public GraphQLRepository() {
        this.client = new GraphQLClient();
    }

    public QueryObject createQueryObject(int toStopIdInt, int fromStopIdInt, Integer viaStopIdInt,
                                         OffsetDateTime offsetDateTime, ArrayList<String> transportModes) {

        // Lager Query objekter med toStop og fromStop + andre parametere dersom brukeren har satt inn mer info
        QueryObject queryObject;
        if (viaStopIdInt == null) {
            if (offsetDateTime == null) {
                if (transportModes.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt);
                } else {
                    if (transportModes.size() > 1) {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, transportModes);
                    } else {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, transportModes.getFirst());
                    }
                }
                // tid/dato er satt inn
            } else {
                if (transportModes.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime);
                } else {
                    if (transportModes.size() > 1) {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportModes);
                    } else {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportModes.getFirst());
                    }
                }
            }
        } else {
            if (offsetDateTime == null) {
                if (transportModes.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt);
                } else {
                    if (transportModes.size() > 1) {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, transportModes);
                    } else {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, transportModes.getFirst());
                    }
                }
            } else {
                if (transportModes.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, offsetDateTime);
                } else {
                    if (transportModes.size() > 1) {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportModes);
                    } else {
                        queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportModes.getFirst());
                    }
                }
            }
        }
        return queryObject;
    }

    public List<TripPattern> getTransportRoutes(QueryObject queryObject) throws Exception {
        EnturResponseService responseService = new EnturResponseService();
        GraphQLQuery queryBuilder = new GraphQLQuery(queryObject);
        String body = queryBuilder.getQueryBasedOnProvidedParameters(queryObject);

        String jsonResponse = client.sendGraphQLRequest(body);

        System.out.println("body : " + body);
        System.out.println("jsonResponse : " + jsonResponse);

        EnturResponse response = responseService.getEnturResponse(jsonResponse);
        List<TripPattern> allTrips;

        if (response != null) {
            if (response.data.trip.tripPatterns != null) {
                allTrips = response.data.trip.tripPatterns;
            } else
                return null;
        } else
            return null;
        // returnerer liste av alle tilgjengelige ruter
        return allTrips;
    }

    @NotNull
    public ArrayList<Map<String, Object>> formatTripPatterns(List<TripPattern> response) {
        ArrayList<Map<String, Object>> formattedTrips = new ArrayList<>();

        // Henter ut all reiseinfo fra hver rute og lagrer i eget hashmap
        for (TripPattern tripPattern : response) {

            for (int i = 0; i < tripPattern.legs.size(); i++) {
                Map<String, Object> trip = new HashMap<>();

                trip.put("transportMode", tripPattern.legs.get(i).mode);
                trip.put("duration", tripPattern.duration);
                if (tripPattern.legs.get(i).line != null) {
                    trip.put("routeName", tripPattern.legs.get(i).line.name);
                    trip.put("authorityName", tripPattern.legs.get(i).line.authority.name);
                    trip.put("publicCode", tripPattern.legs.get(i).line.publicCode);
                }
                trip.put("startStop", tripPattern.legs.get(i).fromPlace.name);
                trip.put("endStop", tripPattern.legs.get(i).toPlace.name);
                trip.put("startTime", tripPattern.aimedStartTime);
                trip.put("endTime", tripPattern.aimedEndTime);
                trip.put("legDuration", tripPattern.legs.get(i).duration);

                // Legger hashmap-en i sitt eget indeks i ArrayList-en
                formattedTrips.add(trip);
            }
        }
        System.out.println("formatted trips: " + formattedTrips);
        return formattedTrips;
    }
}