package org.gruppe4.controllers;

import graphql.dto.*;
import graphql.query.QueryObject;
import io.javalin.http.Context;
import org.gruppe4.enums.TransportType;
import org.gruppe4.repository.GraphQLRepository;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQLController {
    private final GraphQLRepository graphQLRepository;

    public GraphQLController(GraphQLRepository repository) {
        this.graphQLRepository = repository;
    }

    public void getTransportRoutes(Context context) throws Exception {
        String body = context.body();

        // Parse JSON kropp
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> jsonMap = gson.fromJson(body, type);

        // Hent ut parameterne
        String fromStopId = (String) jsonMap.get("start");
        String toStopId = (String) jsonMap.get("end");
        ArrayList<String> transportModes = (ArrayList<String>) jsonMap.get("transportType");
        String viaStopId = context.formParam("viaStop");

        // input=date og input=time. Vurderer input=datetime istedenfor, men det kommer senere
        String dateString = context.formParam("date");
        String timeString = context.formParam("time");

        int toStopIdInt = 0;
        int fromStopIdInt = 0;
        Integer viaStopIdInt = 0;

        ArrayList<String> formattedTransportModes = new ArrayList<>();
        // test
        System.out.println("Controller test transportModes: " + transportModes);
        for (String mode : transportModes) {
            System.out.println(mode);
        }

        if (transportModes != null && !transportModes.isEmpty()) {
            for ( int i = 0; i < transportModes.size(); i++ ) {
                String mode = transportModes.get(i);
                try {
                    // Enum conversion and processing
                    System.out.println("Controller test transportModes.size(): " + transportModes.size());
                    String transport = TransportType.valueOf(mode).getMode();
                    formattedTransportModes.add(i, transport);
                    System.out.println("Controller test TransportType.valueOf() for-loop: " + TransportType.valueOf(mode));
                    System.out.println("Controller test formattedTransportModes for-loop: " + transport);
                } catch (IllegalArgumentException e) {
                    // Log en spesifik feilmelding hvis den typene er ukjent/genererer en feil
                    System.err.println("Ukjent transport type: " + mode);
                }
            }
        }
        System.out.println("Controller test formattedTransportModes final result: " + formattedTransportModes);

        try {
            // sjekker at fromStopId og toStopId ikke er tomme
            if ((fromStopId != null && !fromStopId.isEmpty()) && (toStopId != null && !toStopId.isEmpty())) {
                //context.result("Please enter a valid stop ID");
                fromStopIdInt = Integer.parseInt(fromStopId);
                toStopIdInt = Integer.parseInt(toStopId);
                viaStopIdInt = null;

                if (viaStopId != null)
                    viaStopIdInt = Integer.parseInt(viaStopId);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Ukjent fromStop: " + fromStopId);
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

        // bruker "custom" tid, dette for å teste siden er ikke lagt inn inputfelt for tid ennå
        //OffsetDateTime dTime = OffsetDateTime.parse("2025-11-07T14:00:00.402+01:00");
        QueryObject query = graphQLRepository.createQueryObject(fromStopIdInt, toStopIdInt, viaStopIdInt, offsetDateTime, formattedTransportModes);

        List<TripPattern> response = graphQLRepository.getTransportRoutes(query); // behold, resten under før try er tester

        ArrayList<Map<String, Object>> test = graphQLRepository.formatTripPatterns(response);
        System.out.println("Controller test tripDetails: " + test);
        try {
            if (response != null) {
                ArrayList<Map<String, Object>> tripDetails = graphQLRepository.formatTripPatterns(response);
                System.out.println("Controller test tripDetails: " + tripDetails);
                context.json(tripDetails);
            }
        } catch (Exception e) {
            System.err.println("No trips found");
        }
    }

    /* lages når deserialisering er på plass
    public void checkForDelayedTransports(Context context) {

    }*/
}