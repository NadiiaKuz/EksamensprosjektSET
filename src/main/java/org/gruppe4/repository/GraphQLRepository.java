package org.gruppe4.repository;
import org.gruppe4.graphql.Service.EnturResponseService;
import org.gruppe4.graphql.client.GraphQLClient;
import org.gruppe4.graphql.dto.EnturResponse;
import org.gruppe4.graphql.dto.Leg;
import org.gruppe4.graphql.dto.Trip;
import org.gruppe4.graphql.dto.TripPattern;
import org.gruppe4.graphql.query.GraphQLQuery;
import org.gruppe4.graphql.query.QueryObject;

import java.time.OffsetDateTime;
import java.util.*;
import java.lang.Object;

public class GraphQLRepository {
    private final GraphQLClient client;

    public GraphQLRepository() {
        this.client = new GraphQLClient();
    }

    public QueryObject createQueryObject(int fromStopIdInt, int toStopIdInt, Integer viaStopIdInt,
                                         OffsetDateTime offsetDateTime, ArrayList<String> transportModes) {

        // Lager Query objekter med toStop og fromStop + andre parametere dersom brukeren har satt inn mer info
        QueryObject queryObject;
        if (viaStopIdInt == null) {
            if (transportModes.isEmpty()) {
                queryObject = new QueryObject(fromStopIdInt, toStopIdInt, offsetDateTime);
            } else {
                if (transportModes.size() > 1) {
                    queryObject = new QueryObject(fromStopIdInt, toStopIdInt, offsetDateTime, transportModes);
                } else {
                    queryObject = new QueryObject(fromStopIdInt, toStopIdInt, offsetDateTime, transportModes.getFirst());
                }
            }
        } else {
            if (transportModes.isEmpty()) {
                queryObject = new QueryObject(fromStopIdInt, toStopIdInt, viaStopIdInt, offsetDateTime);
            } else {
                if (transportModes.size() > 1) {
                    queryObject = new QueryObject(fromStopIdInt, toStopIdInt, viaStopIdInt, offsetDateTime, transportModes);
                } else {
                    queryObject = new QueryObject(fromStopIdInt, toStopIdInt, viaStopIdInt, offsetDateTime, transportModes.getFirst());
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
        ArrayList<Map<String, Object>> formattedTripPatterns = new ArrayList<>();

        // Itererer over hver rute i responsen
        for (TripPattern tripPattern : response.tripPatterns) {
            Map<String, Object> tripPatternData = new HashMap<>();
            ArrayList<Map<String, Object>> legsData = new ArrayList<>();

            // Henter informasjon som er spesifikk for tripPattern
            tripPatternData.put("aimedStartTime", tripPattern.aimedStartTime.substring(11, 19));
            tripPatternData.put("aimedEndTime", tripPattern.aimedEndTime.substring(11, 19));
            tripPatternData.put("expectedStartTime", tripPattern.expectedStartTime.substring(11, 19));
            tripPatternData.put("expectedEndTime", tripPattern.expectedEndTime.substring(11, 19));
            tripPatternData.put("tripDuration", tripPattern.duration / 60);

            // Itererer over hvert leg i reisen og samler dem for tripPattern
            for (Leg leg : tripPattern.legs) {
                Map<String, Object> legData = new HashMap<>();

                // Legger til relevant rute info
                if (leg.line != null) {
                    legData.put("routeName", leg.line.name);
                    legData.put("authorityName", leg.line.authority.name);
                    legData.put("publicCode", leg.line.publicCode);
                }

                // Legger til generell leg-informasjon (transportmode, stops, times)
                legData.put("transportMode", leg.mode);
                legData.put("startStop", leg.fromPlace.name);
                legData.put("endStop", leg.toPlace.name);
                legData.put("legStartTime", leg.aimedStartTime.substring(11, 19));
                legData.put("legEndTime", leg.aimedEndTime.substring(11, 19));
                legData.put("legDuration", leg.duration / 60);
                legData.put("distance", String.format("%.0f", leg.distance));

                // Legger leg-data til listen av leg-data
                legsData.add(legData);
            }

            // Legger til legs i tripPattern-dataen
            tripPatternData.put("legs", legsData);

            // Legger det formatterte tripPatternet med alle sine legs i resultat-listen
            formattedTripPatterns.add(tripPatternData);
        }

        return formattedTripPatterns;
    }
}