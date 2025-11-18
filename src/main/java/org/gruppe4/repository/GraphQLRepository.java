package org.gruppe4.repository;
import graphql.Service.EnturResponseService;
import graphql.client.GraphQLClient;
import graphql.dto.*;
import graphql.query.*;

import java.time.OffsetDateTime;
import java.util.*;
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
            if (transportModes.isEmpty()) {
                queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime);
            } else {
                if (transportModes.size() > 1) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportModes);
                } else {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportModes.getFirst());
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
        return queryObject;
    }

    public Trip getTransportRoutes(QueryObject queryObject) throws Exception {
        EnturResponseService responseService = new EnturResponseService();
        GraphQLQuery queryBuilder = new GraphQLQuery(queryObject);
        String body = queryBuilder.getQueryBasedOnProvidedParameters(queryObject);

        String jsonResponse = client.sendGraphQLRequest(body);

        EnturResponse response = responseService.getEnturResponse(jsonResponse);
        Trip allTrips;

        if (response != null) {
            if (response.data.trip.tripPatterns != null) {
                allTrips = response.data.trip;
            } else
                return null;
        } else
            return null;
        // returnerer liste av alle tilgjengelige ruter (eller null ved feil)
        return allTrips;
    }


    public ArrayList<Map<String, Object>> formatTripPatterns(Trip response) {
        ArrayList<Map<String, Object>> formattedTrips = new ArrayList<>();

        // Henter ut all reiseinfo fra hver rute og lagrer i eget hashmap
        for (TripPattern tripPattern : response.tripPatterns) {

            for (Leg leg : tripPattern.legs) {
                Map<String, Object> data = new HashMap<>();

                if (leg.line != null) {
                    data.put("routeName", leg.line.name);
                    data.put("authorityName", leg.line.authority.name);
                    data.put("publicCode", leg.line.publicCode);
                }

                data.put("transportMode", leg.mode);
                data.put("duration", tripPattern.duration / 60);
                data.put("startStop", leg.fromPlace.name);
                data.put("endStop", leg.toPlace.name);
                data.put("startTime", tripPattern.aimedStartTime.substring(11, 19));
                data.put("endTime", tripPattern.aimedEndTime.substring(11, 19));
                data.put("legStartTime", leg.aimedStartTime.substring(11, 19));
                data.put("legEndTime", leg.aimedEndTime.substring(11, 19));
                data.put("legDuration", leg.duration / 60);
                data.put("legSize", tripPattern.legs.size());

                // Legger hashmap-en i eget indeks i ArrayList-en
                formattedTrips.add(data);
            }
            System.out.println(Arrays.toString(tripPattern.legs.toArray()));
        }
        return formattedTrips;
    }
}