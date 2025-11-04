package org.gruppe4.controllers;

import com.sun.jdi.connect.Transport;
import graphql.dto.TripPattern;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import io.javalin.http.Context;
import org.gruppe4.enums.TransportType;
import org.gruppe4.repository.GraphQLRepository;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GraphQLController {
    private GraphQLRepository graphQLRepository;

    public GraphQLController(GraphQLRepository graphQLRepository) {
        this.graphQLRepository = graphQLRepository;
    }

    public void getTransportRoutes(Context context) throws Exception {
        String fromStopId = context.formParam("startStop");
        String toStopId = context.formParam("endStop");
        String viaStopId = context.formParam("viaStop");
        // input=date og input=time. Vurderer input=datetime istedenfor, men det kommer senere
        String dateString = context.formParam("date");
        String timeString = context.formParam("time");

        // prøver med kun 1 transportMode for nå
        String transportMode = context.formParam("type");
        String tMode = TransportType.valueOf(transportMode).getMode();

        if (fromStopId.isEmpty() || toStopId.isEmpty()) {
            context.result("Please enter a valid stop ID");
        } else {
            int fromStopIdInt = Integer.parseInt(fromStopId);
            int toStopIdInt = Integer.parseInt(toStopId);
            Integer viaStopIdInt = null;

            if (viaStopId != null) {
                viaStopIdInt = Integer.parseInt(viaStopId);
            }

            LocalDate date;
            LocalTime time;

            // formatene brukes til LocalDate's parse metode
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime dateTime;

            // LocalDateTime skal parses til OffsetDateTime, Entur bruker dette tidsformatet
            OffsetDateTime offsetDateTime = null;

            // dersom bruker har valgt f.eks kun tid, så formateres det med nåværende dato
            if (dateString != null || timeString != null) {
                if (dateString != null & !dateString.isEmpty()) {
                    date = LocalDate.now();
                } else {
                    date = LocalDate.parse(dateString, timeFormatter);
                }

                if (timeString.isEmpty()) {
                    time = LocalTime.now();
                } else {
                    time = LocalTime.parse(timeString, dateFormatter);
                }

                dateTime = LocalDateTime.of(date, time);
                offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.ofHours(1));
            }

            OffsetDateTime dTime = OffsetDateTime.parse("2025-11-05T14:00:00.402+01:00");
            QueryObject query = graphQLRepository.createQueryObject(fromStopIdInt, toStopIdInt, viaStopIdInt, dTime, tMode);

            ArrayList<TripPattern> response = graphQLRepository.getTransportRoutes(query);
            if (response == null) {
                context.result("No transport routes found");
            } else {
                context.result(response.get(0).aimedStartTime);
            }
            //}
        }

    /* lages når deserialisering er på plass
    public void checkForDelayedTransports(Context context) {

    }*/
    }
}