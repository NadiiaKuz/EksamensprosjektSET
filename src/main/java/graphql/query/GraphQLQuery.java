package graphql.query;
import com.google.gson.Gson;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphQLQuery {

    private String returnJsonQuery(String query) {
        // Create a map to hold the query
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("query", query);

        // Use Gson to convert the map to JSON
        Gson gson = new Gson();

        // Print the JSON output
        return gson.toJson(queryMap);
    }

    public String getQuery(int fromStop, int toStop) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateString = dateTime.toString();
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
                      }
                    }
                    fromPlace {
                      name
                      longitude
                      latitude
                    }
                    toPlace {
                      name
                      longitude
                      latitude
                    }
                  }
                }
                """, fromStop, toStop, dateString);
        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, OffsetDateTime dateTime) {
        String dateString = dateTime.toString();
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
                      }
                    }
                    fromPlace {
                      name
                      longitude
                      latitude
                    }
                    toPlace {
                      name
                      longitude
                      latitude
                    }
                  }
                }
                """, fromStop, toStop, dateString);
        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, String transportMode) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateString = dateTime.toString();
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
                      }
                    }
                    fromPlace {
                      name
                      longitude
                      latitude
                    }
                    toPlace {
                      name
                      longitude
                      latitude
                    }
                  }
                }
                """, fromStop, toStop, dateString, transportMode);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, OffsetDateTime dateTime, String transportMode) {
        String timeString = dateTime.toString();
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
                      }
                    }
                    fromPlace {
                      name
                      longitude
                      latitude
                    }
                    toPlace {
                      name
                      longitude
                      latitude
                    }
                  }
                }
                """, fromStop, toStop, timeString, transportMode);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, ArrayList<String> transportModes) {
        OffsetDateTime dateTime = OffsetDateTime.now();
        String dateString = dateTime.toString();

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
                      }
                    }
                    fromPlace {
                      name
                      longitude
                      latitude
                    }
                    toPlace {
                      name
                      longitude
                      latitude
                    }
                  }
                }
                """, fromStop, toStop, dateString, tModes);

        return returnJsonQuery(graphQLQuery);
    }

    public String getQuery(int fromStop, int toStop, OffsetDateTime dateTime, ArrayList<String> transportModes) {
        String dateString = dateTime.toString();

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
                      }
                    }
                    fromPlace {
                      name
                      longitude
                      latitude
                    }
                    toPlace {
                      name
                      longitude
                      latitude
                    }
                  }
                }
                """, fromStop, toStop, dateString, tModes);

        return returnJsonQuery(graphQLQuery);
    }
}
