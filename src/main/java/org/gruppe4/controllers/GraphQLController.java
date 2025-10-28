package org.gruppe4.controllers;

import com.sun.jdi.connect.Transport;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import io.javalin.http.Context;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GraphQLController {
    private GraphQLQuery graphQLQuery;

    public GraphQLController(GraphQLQuery graphQLQuery) {
        this.graphQLQuery = graphQLQuery;
    }

    public void getTransportRoutes(Context context) {
        String fromStopId = context.formParam("fromStopId");
        String toStopId = context.formParam("toStopId");

        String viaStopId = context.formParam("viaStopId");

        String dateString = context.formParam("date");
        String timeString = context.formParam("time");

        ArrayList<String> transportTypes = new ArrayList<>();
        transportTypes.add(context.formParam("transportTypes"));

        if (fromStopId.isEmpty() || toStopId.isEmpty()) {
            context.result("Please enter a valid stop ID");
        } else {
            int fromStopIdInt = Integer.parseInt(fromStopId);
            int toStopIdInt = Integer.parseInt(toStopId);
            int viaStopIdInt = Integer.parseInt(viaStopId);

            LocalDate date;
            LocalTime time;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            if (!dateString.isEmpty() || !timeString.isEmpty()) {
                if (dateString.isEmpty()) {
                    date = LocalDate.now();
                } else {
                    date = LocalDate.parse(dateString, timeFormatter);
                }

                if (timeString.isEmpty()) {
                    time = LocalTime.now();
                } else {
                    time = LocalTime.parse(timeString, dateFormatter);
                }

                LocalDateTime dateTime = LocalDateTime.of(date, time);
                OffsetDateTime offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.ofHours(1));

            }


        }
    }

    public void checkForDelayedTransports(Context context) {

    }


}
