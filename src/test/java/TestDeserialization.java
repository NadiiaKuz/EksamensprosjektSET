import com.google.gson.Gson;
import graphql.dto.EnturResponse;

public class TestDeserialization {
    public static void main(String[] args) {

        try {

            String json = "{\n" +
                    "    \"data\": {\n" +
                    "        \"trip\": {\n" +
                    "            \"tripPatterns\": [\n" +
                    "                {\n" +
                    "                    \"expectedStartTime\": \"2025-10-02T16:03:00+02:00\",\n" +
                    "                    \"expectedEndTime\": \"2025-10-02T17:20:00+02:00\",\n" +
                    "                    \"duration\": 4620,\n" +
                    "                    \"legs\": [\n" +
                    "                        {\n" +
                    "                            \"mode\": \"bus\",\n" +
                    "                            \"distance\": 153.36,\n" +
                    "                            \"line\": {\n" +
                    "                                \"publicCode\": \"32\",\n" +
                    "                                \"transportMode\": \"bus\",\n" +
                    "                                \"operator\": {\n" +
                    "                                    \"id\": \"OST:Operator:923\"\n" +
                    "                                }\n" +
                    "                            }\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"mode\": \"bus\",\n" +
                    "                            \"distance\": 38045.29,\n" +
                    "                            \"line\": {\n" +
                    "                                \"publicCode\": \"630\",\n" +
                    "                                \"transportMode\": \"bus\",\n" +
                    "                                \"operator\": {\n" +
                    "                                    \"id\": \"OST:Operator:923\"\n" +
                    "                                }\n" +
                    "                            }\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"mode\": \"foot\",\n" +
                    "                            \"distance\": 122.87,\n" +
                    "                            \"line\": null\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                            \"mode\": \"bus\",\n" +
                    "                            \"distance\": 933.26,\n" +
                    "                            \"line\": {\n" +
                    "                                \"publicCode\": \"4\",\n" +
                    "                                \"transportMode\": \"bus\",\n" +
                    "                                \"operator\": {\n" +
                    "                                    \"id\": \"OST:Operator:254\"\n" +
                    "                                }\n" +
                    "                            }\n" +
                    "                        }\n" +
                    "                    ]\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            Gson gson = new Gson();

            EnturResponse response = gson.fromJson(json, EnturResponse.class);

            System.out.println("Start: " + response.data.trip.tripPatterns.get(0).expectedStartTime);
            System.out.println("Duration: " + response.data.trip.tripPatterns.get(0).duration);
            System.out.println("Mode: (leg1) " + response.data.trip.tripPatterns.get(0).legs.get(0).mode);

        } catch (Exception e) {
            System.out.println("There was an error " + e.getMessage());
        }
    }
}
