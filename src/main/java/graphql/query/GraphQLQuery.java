package graphql.query;
import com.google.gson.Gson;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphQLQuery {
    QueryObject queryObject;
    
    public GraphQLQuery(QueryObject queryObject) {
        this.queryObject = queryObject;
    }

    private String returnJsonQuery(String query) {
        // lager map for å holde query
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("query", query);

        // bruker Gson for å konvertere map til JSON
        // dette gjøres fordi Entur aksepterer kun JSON forespørsler
        Gson gson = new Gson();

        return gson.toJson(queryMap);
    }


    public String getQueryBasedOnProvidedParameters(QueryObject queryObject) {
        String query = "";

        // vanlige fra-til forespørsler (uten viaStopp variabel)
        if (queryObject.getViaStop() == null) {

            // brukeren har ikke spesifisert tid/dato, da default-er Entur til nåværende tidspunkt
            if (queryObject.getDateTime() == null) {

                // bruker har ikke valgt transporttype
                if (queryObject.getTransportMode() == null && queryObject.getTransportModes() == null) {
                    // forespørsel med kun start- og sluttstopp parametere
                    query = getQuery(queryObject.getToStop(), queryObject.getFromStop());
                }
                // forespørsel med fra-til, og (1) transporttype som parametere
                else if (queryObject.getTransportMode() != null) {
                    query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                            queryObject.getTransportMode());
                }

                // forespørsel med fra-til, og flere transporttyper som parametere
                else if (queryObject.getTransportModes() != null) {
                    query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                            queryObject.getTransportModes());
                }
            }
            // bruker har spesifisert tid og/eller dato
            else {
                if (queryObject.getTransportMode() == null && queryObject.getTransportModes() == null) {
                    query = getQuery(queryObject.getToStop(), queryObject.getFromStop(), queryObject.getDateTime());
                }
                else {
                    if (queryObject.getTransportMode() != null) {
                        query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                                queryObject.getDateTime(), queryObject.getTransportMode());
                    }

                    else if (queryObject.getTransportModes() != null) {
                        query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                                queryObject.getDateTime(), queryObject.getTransportModes());
                    }
                }
            }
        }
        // med viaStop variabelen
        else {
            if (queryObject.getDateTime() == null) {
                if (queryObject.getTransportMode() == null && queryObject.getTransportModes() == null) {
                    query = getQuery(queryObject.getToStop(), queryObject.getFromStop(), queryObject.getViaStop());
                } else {
                    if (queryObject.getTransportMode() != null) {
                        query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                                queryObject.getViaStop(), queryObject.getTransportMode());
                    }

                    else if (queryObject.getTransportModes() != null) {
                        query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                                queryObject.getViaStop(), queryObject.getTransportModes());
                    }
                }
            }
            else {
                if (queryObject.getTransportMode() == null && queryObject.getTransportModes() == null) {
                    query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                            queryObject.getViaStop(), queryObject.getDateTime());
                }
                else {
                    if (queryObject.getTransportMode() != null) {
                        query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                                queryObject.getViaStop(), queryObject.getDateTime(),
                                queryObject.getTransportMode());
                    }

                    else if (queryObject.getTransportModes() != null) {
                        query = getQuery(queryObject.getToStop(), queryObject.getFromStop(),
                                queryObject.getViaStop(), queryObject.getDateTime(),
                                queryObject.getTransportModes());
                    }
                }
            }
        }

        return query;
    }


    public String getQuery(int fromStop, int toStop) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateTimeString = dateTime.toString();
        String graphQLQuery = String.format("""
                  {
                  trip(
                    from: {place:"NSR:StopPlace:%d"}
                    to: {place:"NSR:StopPlace:%d"}
                    dateTime: "%s"
                  ) {
                    tripPatterns {
                      aimedStartTime
                      aimedEndTime
                      expectedStartTime
                      expectedEndTime
                      duration
                      legs {
                        mode
                        distance
                        line {
                          name
                          publicCode
                          transportMode
                          authority {
                            name
                          }
                        }
                      }
                    }
                    fromPlace {
                      name
                    }
                    toPlace {
                      name
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, OffsetDateTime dateTime) {
        String dateTimeString = dateTime.toString();
        String graphQLQuery = String.format("""
                  {
                  trip(
                    from: {place:"NSR:StopPlace:%d"}
                    to: {place:"NSR:StopPlace:%d"}
                    dateTime: "%s"
                  ) {
                    tripPatterns {
                      aimedStartTime
                      aimedEndTime
                      expectedStartTime
                      expectedEndTime
                      duration
                      legs {
                        mode
                        distance
                        line {
                          name
                          publicCode
                          transportMode
                          authority {
                            name
                          }
                        }
                      }
                    }
                    fromPlace {
                      name
                    }
                    toPlace {
                      name
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, String transportMode) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateTimeString = dateTime.toString();
        String graphQLQuery = String.format("""
                {
                  trip(
                    from: {place: "NSR:StopPlace:%d"}
                    to: {place: "NSR:StopPlace:%d"}
                    dateTime: "%s"
                    modes: {transportModes: {%s}}
                  ) {
                    tripPatterns {
                      aimedStartTime
                      aimedEndTime
                      expectedStartTime
                      expectedEndTime
                      duration
                      legs {
                        mode
                        distance
                        line {
                          name
                          publicCode
                          transportMode
                          authority {
                            name
                          }
                        }
                      }
                    }
                    fromPlace {
                      name
                    }
                    toPlace {
                      name
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, transportMode);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, OffsetDateTime dateTime, String transportMode) {
        String dateTimeString = dateTime.toString();
        String graphQLQuery = String.format("""
                {
                  trip(
                    from: {place: "NSR:StopPlace:%d"}
                    to: {place: "NSR:StopPlace:%d"}
                    dateTime: "%s"
                    modes: {transportModes: {transportMode: %s}}
                  ) {
                    tripPatterns {
                      aimedStartTime
                      aimedEndTime
                      expectedStartTime
                      expectedEndTime
                      duration
                      legs {
                        mode
                        distance
                        line {
                          name
                          publicCode
                          transportMode
                          authority {
                            name
                          }
                        }
                      }
                    }
                    fromPlace {
                      name
                    }
                    toPlace {
                      name
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, transportMode);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, ArrayList<String> transportModes) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateTimeString = dateTime.toString();
        String tModes = String.join("}, {transportMode: ", transportModes);

        String graphQLQuery = String.format("""
                {
                  trip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    dateTime: "%s"
                    modes: {transportModes: [{transportMode: %s}]}
                  ) {
                    tripPatterns {
                      aimedStartTime
                      aimedEndTime
                      expectedStartTime
                      expectedEndTime
                      duration
                      legs {
                        mode
                        distance
                        line {
                          name
                          publicCode
                          transportMode
                          authority {
                            name
                          }
                        }
                      }
                    }
                    fromPlace {
                      name
                    }
                    toPlace {
                      name
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, tModes);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, OffsetDateTime dateTime,
                           ArrayList<String> transportModes) {

        String dateTimeString = dateTime.toString();
        String tModes = String.join("}, {transportMode: ", transportModes);
        String graphQLQuery = String.format("""
                {
                  trip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    dateTime: "%s"
                    modes: {transportModes: [{transportMode: %s}]}
                  ) {
                    tripPatterns {
                      aimedStartTime
                      aimedEndTime
                      expectedStartTime
                      expectedEndTime
                      duration
                      legs {
                        mode
                        distance
                        line {
                          name
                          publicCode
                          transportMode
                          authority {
                            name
                          }
                        }
                      }
                    }
                    fromPlace {
                      name
                    }
                    toPlace {
                      name
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, tModes);

        return returnJsonQuery(graphQLQuery);
    }

    // alt herfra blir "viaTrip" queries, altså en rute med et planlagt byttepunkt

    public String getQuery(int fromStop, int toStop, int viaStop) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateTimeString = dateTime.toString();
        String query = String.format("""
                {
                  viaTrip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    searchWindow: "PT2H"
                    dateTime: "%s"
                    via: [{place: "NSR:StopPlace:%s", minSlack: "PT120S", maxSlack: "PT2H"}]
                  ) {
                    routingErrors {
                      description
                      inputField
                      code
                    }
                    tripPatternsPerSegment {
                      tripPatterns {
                        aimedStartTime
                        aimedEndTime
                        expectedStartTime
                        expectedEndTime
                        duration
                        legs {
                          distance
                          line {
                            name
                            publicCode
                            transportMode
                            operator {
                              id
                            }
                          }
                          fromPlace {
                            name
                          }
                          toPlace {
                            name
                          }
                        }
                      }
                    }
                    tripPatternCombinations {
                      from
                      to
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, viaStop);

        return returnJsonQuery(query);
    }

    public String getQuery(int fromStop, int toStop, int viaStop, OffsetDateTime dateTime) {
        String dateTimeString = dateTime.toString();
        String query = String.format("""
                {
                  viaTrip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    searchWindow: "PT2H"
                    dateTime: "%s"
                    via: [{place: "NSR:StopPlace:%s", minSlack: "PT120S", maxSlack: "PT2H"}]
                  ) {
                    routingErrors {
                      description
                      inputField
                      code
                    }
                    tripPatternsPerSegment {
                      tripPatterns {
                        aimedStartTime
                        aimedEndTime
                        expectedStartTime
                        expectedEndTime
                        duration
                        legs {
                          distance
                          line {
                            name
                            publicCode
                            transportMode
                            operator {
                              id
                            }
                          }
                          fromPlace {
                            name
                          }
                          toPlace {
                            name
                          }
                        }
                      }
                    }
                    tripPatternCombinations {
                      from
                      to
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, viaStop);

        return returnJsonQuery(query);
    }

    public String getQuery(int fromStop, int toStop, int viaStop, String transportMode) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateTimeString = dateTime.toString();
        String query = String.format("""
                {
                  viaTrip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    searchWindow: "PT2H"
                    dateTime: "%s"
                    via: [{place: "NSR:StopPlace:%s", minSlack: "PT120S", maxSlack: "PT2H"}]
                    segments: [{filters: [{select: [{transportModes: [{transportMode: %s}]}]}]},
                    {filters: [{select: [{transportModes: [{transportMode: %s}]}]}]}]
                  ) {
                    routingErrors {
                      description
                      inputField
                      code
                    }
                    tripPatternsPerSegment {
                      tripPatterns {
                        aimedStartTime
                        aimedEndTime
                        expectedStartTime
                        expectedEndTime
                        duration
                        legs {
                          distance
                          line {
                            name
                            publicCode
                            transportMode
                            operator {
                              id
                            }
                          }
                          fromPlace {
                            name
                          }
                          toPlace {
                            name
                          }
                        }
                      }
                    }
                    tripPatternCombinations {
                      from
                      to
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, viaStop, transportMode, transportMode);

        return returnJsonQuery(query);
    }

    public String getQuery(int fromStop, int toStop, int viaStop,
                           OffsetDateTime dateTime, String transportMode) {

        String dateTimeString = dateTime.toString();
        String query = String.format("""
                {
                  viaTrip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    searchWindow: "PT2H"
                    dateTime: "%s"
                    via: [{place: "NSR:StopPlace:%s", minSlack: "PT120S", maxSlack: "PT2H"}]
                    segments: [{filters: [{select: [{transportModes: [{transportMode: %s}]}]}]},
                    {filters: [{select: [{transportModes: [{transportMode: %s}]}]}]}]
                  ) {
                    routingErrors {
                      description
                      inputField
                      code
                    }
                    tripPatternsPerSegment {
                      tripPatterns {
                        aimedStartTime
                        aimedEndTime
                        expectedStartTime
                        expectedEndTime
                        duration
                        legs {
                          distance
                          line {
                            name
                            publicCode
                            transportMode
                            operator {
                              id
                            }
                          }
                          fromPlace {
                            name
                          }
                          toPlace {
                            name
                          }
                        }
                      }
                    }
                    tripPatternCombinations {
                      from
                      to
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, viaStop, transportMode, transportMode);

        return returnJsonQuery(query);
    }

    public String getQuery(int fromStop, int toStop, int viaStop,
                           ArrayList<String> transportModes) {

        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateTimeString = dateTime.toString();
        String tModes = String.join("}, {transportMode: ", transportModes);

        String query = String.format("""
                {
                  viaTrip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    searchWindow: "PT2H"
                    dateTime: "%s"
                    via: [{place: "NSR:StopPlace:%s", minSlack: "PT120S", maxSlack: "PT2H"}]
                    segments: [{filters: [{select: [{transportModes: [{transportMode: %s}]}]}]}, {filters: [{select: [{transportModes: [{transportMode: %s}]}]}]}]
                  ) {
                    routingErrors {
                      description
                      inputField
                      code
                    }
                    tripPatternsPerSegment {
                      tripPatterns {
                        aimedStartTime
                        aimedEndTime
                        expectedStartTime
                        expectedEndTime
                        duration
                        legs {
                          distance
                          line {
                            name
                            publicCode
                            transportMode
                            operator {
                              id
                            }
                          }
                          fromPlace {
                            name
                          }
                          toPlace {
                            name
                          }
                        }
                      }
                    }
                    tripPatternCombinations {
                      from
                      to
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, viaStop, tModes, tModes);

        return returnJsonQuery(query);
    }

    public String getQuery(int fromStop, int toStop, int viaStop,
                           OffsetDateTime dateTime, ArrayList<String> transportModes) {

        String dateTimeString = dateTime.toString();
        String tModes = String.join("}, {transportMode: ", transportModes);
        String query = String.format("""
                {
                  viaTrip(
                    from: {place: "NSR:StopPlace:%s"}
                    to: {place: "NSR:StopPlace:%s"}
                    searchWindow: "PT2H"
                    dateTime: "%s"
                    via: [{place: "NSR:StopPlace:%s", minSlack: "PT120S", maxSlack: "PT2H"}]
                    segments: [{filters: [{select: [{transportModes: [{transportMode: %s}]}]}]}, {filters: [{select: [{transportModes: [{transportMode: %s}]}]}]}]
                  ) {
                    routingErrors {
                      description
                      inputField
                      code
                    }
                    tripPatternsPerSegment {
                      tripPatterns {
                        aimedStartTime
                        aimedEndTime
                        expectedStartTime
                        expectedEndTime
                        duration
                        legs {
                          distance
                          line {
                            name
                            publicCode
                            transportMode
                            operator {
                              id
                            }
                          }
                          fromPlace {
                            name
                          }
                          toPlace {
                            name
                          }
                        }
                      }
                    }
                    tripPatternCombinations {
                      from
                      to
                    }
                  }
                }
                """, fromStop, toStop, dateTimeString, viaStop, tModes, tModes);

        return returnJsonQuery(query);
    }
}