/*import graphql.client.GraphQLClient;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import org.gruppe4.enums.TransportType;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestGraphQL {
    public static void main(String[] args) throws Exception {
        // Lager automatiserte forespørsler senere når vi får brukerinput fra nettsiden
        // Lager klasse for å oversette navn på busstopper til ID senere

        GraphQLClient client = new GraphQLClient();

        String dateString = "2025-11-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        System.out.println(date);

        String timeString = "07:00";
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.parse(timeString));
        OffsetDateTime offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.ofHours(1));


        // Eksempel viaTrip query
        TransportType buss = TransportType.BUS; // enum
        TransportType tog = TransportType.RAIL; // enum
        ArrayList<String> transportModes = new ArrayList<>();
        transportModes.add(buss.getMode()); // bruker BUS enum
        transportModes.add(tog.getMode()); // bruker RAIL enum

        QueryObject queryObject = new QueryObject(60053, 58794, offsetDateTime, transportModes);
        GraphQLQuery query = new GraphQLQuery(queryObject);
*/
        /*
        // Stop ID 60053: Halden stasjon, stop ID 58794: Fredrikstad stasjon
        String response = client.sendGraphQLRequest(query.getQuery(60045, 59872, 62339, customTime, transportModes));
        */

        //String response = client.sendGraphQLRequest(query.getQuery(queryObject.getFromStop(), queryObject.getToStop(), queryObject.getDateTime(), transportModes));
/*
        String response = client.sendGraphQLRequest(query.getQueryBasedOnProvidedParameters());

        System.out.println(response);
    }
}
*/