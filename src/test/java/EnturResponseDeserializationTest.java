import com.google.gson.Gson;
import graphql.dto.EnturResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EnturResponseDeserializationTest {

    @Test
    @DisplayName("Deserialization of Valid Entur JSON provides correct TripPattern-data")
    public void testValidJsonDeserialization() {
        // Arrange
        String json = "{ \"data\": { \"trip\": { \"tripPatterns\": [{ \"aimedStartTime\": \"2025-10-02T16:03:00+02:00\", \"duration\": 4620 }] } } }";
        Gson gson = new Gson();

        // Act
        EnturResponse response = gson.fromJson(json, EnturResponse.class);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.data);
        Assertions.assertNotNull(response.data.trip);
        Assertions.assertFalse(response.data.trip.tripPatterns.isEmpty());
        Assertions.assertEquals(4620, response.data.trip.tripPatterns.get(0).duration);
    }

    @Test
    @DisplayName("Deserialization of invalid JSON throws JsonSyntaxException")
    public void testInvalidJsonDeserialization() {
        // Arrange
        String invalidJson = "{ invalid json }";
        Gson gson = new Gson();

        // Act + Assert
        Assertions.assertThrows(com.google.gson.JsonSyntaxException.class, () -> {
            gson.fromJson(invalidJson, EnturResponse.class);
        });
    }



    @Test
    @DisplayName("Deserialization works even when some fields are missing")
    public void testDeserializationWithMissingFields(){


        String json = "{ \"data\": { \"trip\": { \"tripPatterns\": [{ \"duration\": 1200 }] } } }";
        Gson gson = new Gson();

        EnturResponse response = gson.fromJson(json, EnturResponse.class);

        Assertions.assertNotNull(response.data.trip.tripPatterns.get(0).duration);
        Assertions.assertNull(response.data.trip.tripPatterns.get(0).aimedStartTime);

    }

}
