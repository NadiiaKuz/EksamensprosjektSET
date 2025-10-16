package graphql;

import graphql.client.GraphQLClient;
import graphql.query.GraphQLQuery;
import org.gruppe4.enums.TransportType;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class GraphQLRequestApplication {
    public static void main(String[] args) throws Exception {
        GraphQLClient client = new GraphQLClient();
        GraphQLQuery query = new GraphQLQuery();

        // Lager automatiserte forespørsler senere når vi får brukerinput fra nettsiden
        // Lager klasse for å oversette navn på busstopper til ID senere

        // Eksempel viaTrip query
        TransportType buss = TransportType.BUS; // enum
        TransportType tog = TransportType.RAIL; // enum

        OffsetDateTime customTime = OffsetDateTime.parse("2025-10-15T16:00:00.218+02:00");
        ArrayList<String> transportModes = new ArrayList<>();
        transportModes.add(buss.getMode()); // bruker BUS enum
        transportModes.add(tog.getMode()); // bruker RAIL enum

        // Stop ID 60053: Halden stasjon, stop ID 58794: Fredrikstad stasjon
        String response = client.sendGraphQLRequest(query.getQuery(60045, 59872, 62339, customTime, transportModes));

        System.out.println(response);
    }
}
