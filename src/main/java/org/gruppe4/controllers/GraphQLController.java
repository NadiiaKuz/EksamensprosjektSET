package org.gruppe4.controllers;

import com.sun.jdi.connect.Transport;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import io.javalin.http.Context;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class GraphQLController {
    private GraphQLQuery graphQLQuery;

    public GraphQLController(GraphQLQuery graphQLQuery) {
        this.graphQLQuery = graphQLQuery;
    }

    public void getTransportRoutes(Context context) {
        int fromStopId = Integer.parseInt(context.formParam("fromStopId"));
        int toStopId = Integer.parseInt(context.formParam("toStopId"));
        int viaStopId = Integer.parseInt(context.formParam("viaStopId"));

        // dateTime kan deles opp i dato og klokkeslett, deretter parses til OffsetDateTime
        OffsetDateTime dateTime;

        // bruker skal kunne velge 1 eller flere transportmidler, finn en passende context metode
        String transportMode;
    }

    public void checkForDelayedTransports(Context context) {

    }


}
