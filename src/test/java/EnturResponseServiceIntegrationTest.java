
import graphql.Service.EnturResponseService;
import graphql.dto.EnturResponse;
import graphql.dto.TripPattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EnturResponseServiceIntegrationTest {

    @Test
    @DisplayName("Integrasjonstest – importing actual Entur-response and deserializing correctly")
    public void testGetEnturResponseFromEnturAPI() {

        // Arrange
        EnturResponseService service = new EnturResponseService();
        int fromStopId = 60053; // Halden
        int toStopId = 58794;   // Fredrikstad

        // Act
        EnturResponse response = service.getEnturResponse(fromStopId, toStopId);

        // Assert
        Assertions.assertNotNull(response, "Response from Entur is null");
        Assertions.assertNotNull(response.data, "Response is missing 'data'-objekt");
        Assertions.assertNotNull(response.data.trip, "Response is missing 'trip'-objekt");
        Assertions.assertNotNull(response.data.trip.tripPatterns, "Response is missing 'tripPatterns'-list");
        Assertions.assertFalse(response.data.trip.tripPatterns.isEmpty(), "No trips found for chosen route");

        TripPattern tripPattern = response.data.trip.tripPatterns.get(0);

        Assertions.assertNotNull(tripPattern.aimedStartTime, "Missing aimedStartTime");
        Assertions.assertNotNull(tripPattern.aimedEndTime, "Missing aimedEndTime");
        Assertions.assertTrue(tripPattern.duration > 0, "Duration must be more than 0");

        System.out.println("Trip route successfully received from Entur");
        System.out.println("Aimed StartTime: " + tripPattern.aimedStartTime);
        System.out.println("Aimed EndTime: " + tripPattern.aimedEndTime);
        System.out.println("Duration: " + tripPattern.duration);
        System.out.println("Total legs: " + tripPattern.legs.size());
    }
}


