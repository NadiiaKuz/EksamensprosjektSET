package org.gruppe4.repository;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import java.time.OffsetDateTime;

public class GraphQLRepository {
    public String queryResponse;

    public QueryObject createQueryObject(int toStopIdInt, int fromStopIdInt, Integer viaStopIdInt, OffsetDateTime offsetDateTime, String transportMode) {
        QueryObject queryObject;

        // Lager Query objekter med toStop og fromStop + andre parametere dersom brukeren har satt inn mer info
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

    public String getTransportRoutes(QueryObject queryObject) {
        GraphQLQuery graphQLQuery = new GraphQLQuery(queryObject);
        return graphQLQuery.getQueryBasedOnProvidedParameters();
    }
}
