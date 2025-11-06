package org.gruppe4.repository;
import graphql.Service.EnturResponseService;
import graphql.client.GraphQLClient;
import graphql.dto.EnturResponse;
import graphql.dto.Trip;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import graphql.dto.EnturResponse;
import graphql.dto.TripPattern;
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

        System.out.println("TESTT 1 queryObject: " + queryObject.getDateTime());
       // System.out.println("TESTT 2 queryObject, 1 tType: " + queryObject.getTransportMode());
        System.out.println("TESTT 2 queryObject: " + queryObject.getTransportModes());
        System.out.println(queryObject.transportModes);

        // Tester at queries blir laget riktig
        System.out.println("TESTT 3 getQueryBasedOnProvidedParameters: " + body);

        String jsonResponse = client.sendGraphQLRequest(body);

        System.out.println();
        System.out.println("Repository test jsonResponse: " + jsonResponse);
        System.out.println();

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
    public ArrayList<Map<String, Object>> formatTripPatterns(List<TripPattern> response, QueryObject query) {
        ArrayList<Map<String, Object>> formattedTrips = new ArrayList<>();

        //TripPattern tripP = response.data.trip.tripPatterns.get(0)

        // Henter ut all reiseinfo fra hver rute og lagrer i eget hashmap
        for (TripPattern tripPattern : response) {
            Map<String, Object> trip = new HashMap<>();
            trip.put("startStop", query.getFromStop());
            trip.put("endStop", query.getToStop());
            trip.put("operatorName", tripPattern.legs.get(0).line.operator.id);
            trip.put("publicCode", tripPattern.legs.get(0).line.publicCode);
            trip.put("transportMode", tripPattern.legs.get(0).line.transportMode);
            trip.put("startTime", tripPattern.aimedStartTime);
            trip.put("endTime", tripPattern.aimedEndTime);
            trip.put("duration", tripPattern.duration);

            // Legger hashmapen i sitt eget indeks i ArrayList-en
            formattedTrips.add(trip);
        }
        return formattedTrips;
    }
}