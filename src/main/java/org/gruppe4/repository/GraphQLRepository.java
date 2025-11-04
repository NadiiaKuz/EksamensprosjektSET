package org.gruppe4.repository;
import graphql.Service.EnturResponseService;
import graphql.client.GraphQLClient;
import graphql.dto.EnturResponse;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import graphql.dto.EnturResponse;
import graphql.dto.TripPattern;
import java.util.List;

public class GraphQLRepository {
    private final GraphQLClient client;

    public GraphQLRepository() {
        this.client = new GraphQLClient();
    }

    public QueryObject createQueryObject(int toStopIdInt, int fromStopIdInt, Integer viaStopIdInt,
                                         OffsetDateTime offsetDateTime, String transportMode) {

        // Lager Query objekter med toStop og fromStop + andre parametere dersom brukeren har satt inn mer info
        QueryObject queryObject;
        if (viaStopIdInt == null) {
            if (offsetDateTime == null) {
                if (transportMode.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt);
                } else {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, transportMode);
                }
            } else {
                if (transportMode.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime);
                } else {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportMode);
                }
            }
        } else {
            if (offsetDateTime == null) {
                if (transportMode.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt);
                } else {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, transportMode);
                }
            } else {
                if (transportMode.isEmpty()) {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, offsetDateTime);
                } else {
                    queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, offsetDateTime, transportMode);
                }
            }
        }
        return queryObject;
    }

    public ArrayList<TripPattern> getTransportRoutes(QueryObject queryObject) throws Exception {
        GraphQLQuery queryBuilder = new GraphQLQuery(queryObject);
        String body = queryBuilder.getQueryBasedOnProvidedParameters(queryObject);
        String jsonResponse = client.sendGraphQLRequest(body);
        EnturResponseService responseService = new EnturResponseService();

        EnturResponse response = responseService.getEnturResponse(jsonResponse);
        ArrayList<TripPattern> tripPatterns = new ArrayList<>();

        if (response != null) {
            if (response.data != null) {
                List<TripPattern> allTrips = response.data.trip.tripPatterns;
                tripPatterns.addAll(allTrips);
            } else
                return null;
        } else
            return null;
        // returnerer liste av alle tilgjengelige ruter
        return tripPatterns;
    }
}