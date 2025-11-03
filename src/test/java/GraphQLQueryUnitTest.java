import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;

public class GraphQLQueryUnitTest {

    @Test
    @DisplayName("getQueryBasedOnProvidedParameters should build from-to query correctly")
    public void testBuildSimpleQuery() {

        // Arrange
        QueryObject queryObject = new QueryObject(60053, 58794); // Halden → Fredrikstad
        GraphQLQuery query = new GraphQLQuery(queryObject);

        // Act
        String result = query.getQueryBasedOnProvidedParameters(queryObject);

        // Assert
        Assertions.assertTrue(result.contains("trip("), "Query should contain 'trip('");
        Assertions.assertTrue(result.contains("NSR:StopPlace:60053"), "Should contain fromStop");
        Assertions.assertTrue(result.contains("NSR:StopPlace:58794"), "Should contain toStop");
    }

    @Test
    @DisplayName("Builds query with date and transportypes")
    public void testQueryWithDateTimeAndTransportModes() {

        OffsetDateTime dateTime = OffsetDateTime.parse("2025-10-16T16:00:00+02:00");
        ArrayList<String> modes = new ArrayList<>();
        modes.add("bus");
        modes.add("rail");

        QueryObject queryObject = new QueryObject(60053, 58794, dateTime, modes);
        GraphQLQuery query = new GraphQLQuery(queryObject);

        String builtQuery = query.getQueryBasedOnProvidedParameters(queryObject);

        Assertions.assertTrue(builtQuery.contains("bus"));
        Assertions.assertTrue(builtQuery.contains("rail"));
        Assertions.assertTrue(builtQuery.contains("dateTime"));
    }
}
