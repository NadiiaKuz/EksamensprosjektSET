package org.gruppe4.controllers;

import com.sun.jdi.connect.Transport;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import io.javalin.http.Context;
import org.gruppe4.repository.GraphQLRepository;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GraphQLController {
    private GraphQLRepository graphQLRepository;

    public GraphQLController(GraphQLRepository graphQLRepository) {
        this.graphQLRepository = graphQLRepository;
    }

    public void getTransportRoutes(Context context) {
        String fromStopId = context.formParam("fromStopId");
        String toStopId = context.formParam("toStopId");

        String viaStopId = context.formParam("viaStopId");

        String dateString = context.formParam("date");
        String timeString = context.formParam("time");

        // prøver med kun 1 transportMode for nå
        String transportMode = context.formParam("transportMode");

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
            LocalDateTime dateTime;
            OffsetDateTime offsetDateTime = null;

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

                dateTime = LocalDateTime.of(date, time);
                offsetDateTime = OffsetDateTime.of(dateTime, ZoneOffset.ofHours(1));
            }

            // Lager Query objekter med toStop og fromStop + andre parametere dersom brukeren har satt inn mer info
            if (viaStopId.isEmpty()) {
                if (offsetDateTime == null) {
                    if (transportMode.isEmpty()) {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt);
                        graphQLRepository.getTransportRoutes(queryObject);
                    } else {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, transportMode);
                        graphQLRepository.getTransportRoutes(queryObject);
                    }
                } else {
                    if (transportMode.isEmpty()) {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime);
                        graphQLRepository.getTransportRoutes(queryObject);
                    } else {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, offsetDateTime, transportMode);
                        graphQLRepository.getTransportRoutes(queryObject);
                    }
                }
            } else {
                if (offsetDateTime == null) {
                    if (transportMode.isEmpty()) {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt);
                        graphQLRepository.getTransportRoutes(queryObject);
                    } else {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, transportMode);
                        graphQLRepository.getTransportRoutes(queryObject);
                    }
                } else {
                    if (transportMode.isEmpty()) {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, offsetDateTime);
                        graphQLRepository.getTransportRoutes(queryObject);
                    } else {
                        QueryObject queryObject = new QueryObject(toStopIdInt, fromStopIdInt, viaStopIdInt, offsetDateTime, transportMode);
                        graphQLRepository.getTransportRoutes(queryObject);
                    }
                }
            }

        }
    }

    public void checkForDelayedTransports(Context context) {

    }


}
