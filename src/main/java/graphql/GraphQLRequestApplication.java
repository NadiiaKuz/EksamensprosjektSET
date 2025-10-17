package graphql;

import graphql.client.GraphQLClient;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import org.gruppe4.enums.TransportType;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class GraphQLRequestApplication {
    public static void main(String[] args) throws Exception {
        // Lager automatiserte forespørsler senere når vi får brukerinput fra nettsiden
        // Lager klasse for å oversette navn på busstopper til ID senere

        GraphQLClient client = new GraphQLClient();

        OffsetDateTime customTime = OffsetDateTime.parse("2025-10-16T16:00:00.218+02:00");

        // Eksempel viaTrip query
        TransportType buss = TransportType.BUS; // enum
        TransportType tog = TransportType.RAIL; // enum
        ArrayList<String> transportModes = new ArrayList<>();
        transportModes.add(buss.getMode()); // bruker BUS enum
        transportModes.add(tog.getMode()); // bruker RAIL enum

        QueryObject queryObject = new QueryObject(60053, 58794, customTime, transportModes);
        GraphQLQuery query = new GraphQLQuery(queryObject);

        /*
        // Stop ID 60053: Halden stasjon, stop ID 58794: Fredrikstad stasjon
        String response = client.sendGraphQLRequest(query.getQuery(60045, 59872, 62339, customTime, transportModes));
        */

        //String response = client.sendGraphQLRequest(query.getQuery(query.getFromStop(), query.getToStop(), customTime, transportModes));

        String response = client.sendGraphQLRequest(query.getQuery(queryObject.getFromStop(), queryObject.getToStop(),
                                                    queryObject.getDateTime(), queryObject.getTransportModes()));

        System.out.println(response);
    }
}
