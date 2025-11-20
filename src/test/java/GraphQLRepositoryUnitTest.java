import org.gruppe4.graphql.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.gruppe4.graphql.query.*;
import org.gruppe4.repository.GraphQLRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GraphQLRepositoryUnitTest {

    @Test
    void testCreateQueryObjectWithTransportModes() {
        // Arrange
        GraphQLRepository repository = new GraphQLRepository();
        ArrayList<String> transportModes = new ArrayList<>(Arrays.asList("BUS", "TRAIN"));
        // oppd
        OffsetDateTime dateTime = OffsetDateTime.parse("2025-11-20T09:00:00+01:00");

        // Act
        QueryObject queryObject = repository.createQueryObject(2306, 2534, null, dateTime, transportModes);

        // Assert
        Assertions.assertEquals(2306, queryObject.getFromStop(), "fromStop is incorrecct");
        Assertions.assertTrue(queryObject.getTransportModes().contains("BUS"), "transportModes doesn't contain BUS");
        Assertions.assertTrue(queryObject.getTransportModes().contains("TRAIN"), "transportModes doesn't contain TRAIN");
    }

    @Test
    void testCreateQueryObjectWithViaStopAndNoTransportModes() {
        // Arrange
        GraphQLRepository repository = new GraphQLRepository();
        // Oppdater dateTime til et fremtidig tidspunkt, midt på dagen, dersom resultaten er tom
        OffsetDateTime dateTime = OffsetDateTime.parse("2025-11-20T09:00:00+01:00");
        ArrayList<String> transportModes = new ArrayList<>();
        transportModes.add("BUS");

        // Act
        QueryObject queryObject = repository.createQueryObject(2306, 2534, 1234, dateTime, transportModes);

        // Assert
        Assertions.assertNotNull(queryObject, "queryObject is null");
        Assertions.assertEquals(1234, queryObject.getViaStop());
        Assertions.assertEquals(dateTime, queryObject.getDateTime(), "dateTime is incorrect");
    }

    @Test
    void testGetTransportRoutesValidResponse() throws Exception {
        // Arrange
        GraphQLRepository repository = mock(GraphQLRepository.class);
        Trip mockTrip = new Trip();
        // Oppdater dateTime til et fremtidig tidspunkt, midt på dagen, dersom resulaten er tom
        OffsetDateTime dateTime = OffsetDateTime.parse("2025-11-20T09:00:00+01:00");
        QueryObject queryObject = new QueryObject(2306, 2534, dateTime);
        when(repository.getTransportRoutes(queryObject)).thenReturn(mockTrip);

        // Act
        ArrayList<Map<String, Object>> formattedTripPatterns = repository.formatTripPatterns(mockTrip);

        // Assert
        Assertions.assertNotNull(formattedTripPatterns, "result is null");
        Assertions.assertNotNull(formattedTripPatterns, "tripPatterns is null");

        for (Map<String, Object> tripPattern : formattedTripPatterns) {
            Assertions.assertTrue(tripPattern.get("dateTime").equals(dateTime));
            Assertions.assertTrue(tripPattern.get("transportMode").equals("BUS"), "transportMode doesn't contain BUS");
            Assertions.assertNotNull(tripPattern, "tripPattern is null");
            Assertions.assertNotNull(tripPattern.get("fromStop"), "fromStop is null");
            Assertions.assertNotNull(tripPattern.get("toStop"), "toStop is null");
            Assertions.assertNotNull(tripPattern.get("date"), "date is null");
            Assertions.assertNotNull(tripPattern.get("startTime"), "startTime is null");
            Assertions.assertNotNull(tripPattern.get("endTime"), "endTime is null");
            Assertions.assertNotNull(tripPattern.get("transportMode"), "transportMode is null");
        }
    }

    @Test
    void testFormatTripPatternsValidTripPattern() {
        //Arrange
        GraphQLRepository repository = new GraphQLRepository();

        // Opprette et mock TripPattern
        TripPattern mockTripPattern = new TripPattern();
        mockTripPattern.aimedStartTime = "2025-11-19T08:30:00+01:00"; // "aimed time" er planlagt tid
        mockTripPattern.aimedEndTime = "2025-11-19T09:00:00+01:00";
        mockTripPattern.expectedStartTime = "2025-11-19T08:35:00+01:00"; // "exptected time" er sanntid
        mockTripPattern.expectedEndTime = "2025-11-19T09:05:00+01:00";
        mockTripPattern.duration = 3600; // 1 time i sekunder

        // Opprette mock Leg
        Leg mockLeg = new Leg();
        mockLeg.aimedStartTime = "2025-11-19T08:30:00+01:00";
        mockLeg.aimedEndTime = "2025-11-19T09:00:00+01:00";
        mockLeg.mode = "BUS";
        mockLeg.fromPlace = new FromPlace();
        mockLeg.toPlace = new ToPlace();
        mockLeg.fromPlace.name = "Stop A";
        mockLeg.toPlace.name = "Stop B";
        mockLeg.duration = 1800; // 30 minutter i sekunder
        mockLeg.distance = 10000.0; // 10 km

        // Sett linje på Leg
        mockLeg.line = new Line();
        mockLeg.line.name = "Bus Line 1";
        mockLeg.line.authority = new Authority();
        mockLeg.line.authority.name = "Authority Name";
        mockLeg.line.publicCode = "123";

        // Legg Leg til TripPattern
        mockTripPattern.legs = new ArrayList<>();
        mockTripPattern.legs.add(mockLeg);

        // Opprette mock Trip
        Trip mockTrip = new Trip();
        mockTrip.tripPatterns = new ArrayList<>();
        mockTrip.tripPatterns.add(mockTripPattern);

        // Act
        // Kalle formatTripPatterns
        ArrayList<Map<String, Object>> formattedPatterns = repository.formatTripPatterns(mockTrip);

        // Assert
        Assertions.assertFalse(formattedPatterns.isEmpty());

        Map<String, Object> firstPattern = formattedPatterns.getFirst();
        Assertions.assertEquals("08:30:00", firstPattern.get("aimedStartTime"));
        Assertions.assertEquals(60, firstPattern.get("tripDuration")); // 60 minutter

        ArrayList<Map<String, Object>> legsData = (ArrayList<Map<String, Object>>) firstPattern.get("legs");
        Assertions.assertEquals("BUS", legsData.getFirst().get("transportMode"));
        Assertions.assertEquals("Stop A", legsData.getFirst().get("startStop"));
        Assertions.assertEquals("Stop B", legsData.getFirst().get("endStop"));

        // Skriv ut data fra legs
        for (Map<String, Object> data : legsData) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    @Test
    @DisplayName(" – ")
    public void testGraphQLResponseFromRepository() throws Exception {

        // Arrange
        GraphQLRepository repository = new GraphQLRepository();
        int fromStop = 2306; // Fredrikstad bussterminal
        int toStop = 2534; // Remmen (Halden)
        // juster tiden dersom resultaten er tom, tidligere ruter vises ikke
        OffsetDateTime dateTime = OffsetDateTime.parse("2025-11-20T08:00:00+01:00");
        ArrayList<String> transportModes = new ArrayList<>(); // beholdes tomt
        QueryObject queryObject = repository.createQueryObject(fromStop, toStop, null, dateTime, transportModes);

        // Act
        Trip queryResponse = repository.getTransportRoutes(queryObject);
        ArrayList<Map<String, Object>> tripPatterns = repository.formatTripPatterns(queryResponse);

        // Assert
        Assertions.assertNotNull(queryResponse, "Query response is null");
        Assertions.assertFalse(tripPatterns.isEmpty(), "Trip patterns is empty");


        for (Map<String, Object> tripPattern : tripPatterns) {
            String legs = "tripPattern: " + tripPattern.get("legs");
        }

        for (TripPattern tripPattern : queryResponse.tripPatterns) {
            Assertions.assertFalse(tripPattern.aimedStartTime.isEmpty(), "Start time is empty");
            Assertions.assertFalse(tripPattern.aimedEndTime.isEmpty(), "End time is empty");

            for (Leg leg : tripPattern.legs) {
                Assertions.assertNotNull(leg, "Leg is null");
                Assertions.assertNotNull(leg.fromPlace, "From place is null");
                Assertions.assertNotNull(leg.toPlace, "To place is null");
            }
        }
    }
}
