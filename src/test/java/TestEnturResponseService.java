import graphql.Service.EnturResponseService;
import graphql.dto.EnturResponse;
import graphql.dto.TripPattern;

// Test class to verify that EnturResponseService and JSON deserialization work
//Fetches a trip from Entur API using stop Id's and prints the first trip pattern


public class TestEnturResponseService {
    public static void main(String[] args) {

        EnturResponseService service = new EnturResponseService();

        int fromStopId = 60053;
        int toStopId = 58794;

        EnturResponse response = service.getEnturResponse(fromStopId, toStopId);

        if (response == null) {
            System.out.println("No response from Entur.");
            return;
        }

        if (response.data == null){
            System.out.println("The response is missing 'data' object.");
            return;
        }

        if (response.data.trip == null){
            System.out.println("The response is missing 'trip' object.");
            return;
        }

        if (response.data.trip.tripPatterns == null) {
            System.out.println("The response is missing 'tripPatterns' list.");
            return;
        }

        if (response.data.trip.tripPatterns.isEmpty()){
            System.out.println("No available trips for selected route.");
            return;
        }

        TripPattern tripPattern = response.data.trip.tripPatterns.get(0);

        System.out.println("Trip route successfully received from Entur");
        System.out.println("Aimed time: " + tripPattern.aimedStartTime);
        System.out.println("Aimed time: " + tripPattern.aimedEndTime);
        System.out.println("Duration: " + tripPattern.duration);
        System.out.println("Total legs: " + tripPattern.legs.size());

    }

}
