import org.gruppe4.graphql.query.GraphQLQuery;
import org.gruppe4.graphql.query.QueryObject;
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
        OffsetDateTime dateTime = OffsetDateTime.now();
        QueryObject queryObject = new QueryObject(60053, 58794, dateTime); // Halden stasjon → Fredrikstad stasjon
        GraphQLQuery query = new GraphQLQuery(queryObject);

        // Act
        String result = query.getQueryBasedOnProvidedParameters(queryObject);

        // Assert
        Assertions.assertTrue(result.contains("trip("), "Query should contain 'trip('");
        Assertions.assertTrue(result.contains("NSR:StopPlace:60053"), "Should contain fromStop");
        Assertions.assertTrue(result.contains("NSR:StopPlace:58794"), "Should contain toStop");
    }

    @Test
    @DisplayName("Builds query with date and several transport modes")
    public void testBuildQueryWithDateTimeAndTransportModes() throws Exception {

        // Arrange
        OffsetDateTime dateTime = OffsetDateTime.now();
        ArrayList<String> modes = new ArrayList<>();
        modes.add("bus");
        modes.add("rail");
        String mode = "Bus";
        QueryObject queryObject = new QueryObject(60053, 58794, dateTime, modes);
        GraphQLQuery query = new GraphQLQuery(queryObject);

        // Act
        String builtQuery = query.getQueryBasedOnProvidedParameters(queryObject);

        Assertions.assertTrue(builtQuery.contains("bus"), "Query should contain 'bus'");
        Assertions.assertTrue(builtQuery.contains("rail"), "Query should contain 'rail'");
        Assertions.assertTrue(builtQuery.contains("dateTime"), "Query should contain 'dateTime'");
        Assertions.assertTrue(builtQuery.contains("mode"), "Query should contain 'mode'");
        Assertions.assertTrue(builtQuery.contains("60053"), "Query should contain fromStop");
        Assertions.assertTrue(builtQuery.contains("58794"), "Query should contain toStop");
        Assertions.assertTrue(builtQuery.contains("dateTime"), "Query should contain 'dateTime'" );
        System.out.println(builtQuery);
    }
}
