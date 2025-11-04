package graphql.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import graphql.dto.EnturResponse;

public class EnturResponseService {
    private final Gson gson;

    public EnturResponseService() {
        this.gson = new Gson();
    }


    private String removeResponsePrefix(String text) {
        if (text == null) {
            return "";
        }
        String prefix = "Response:";
        if (text.startsWith(prefix)) {
            text = text.substring(prefix.length());
        }
        return text.trim();
    }


    public EnturResponse getEnturResponse(String jsonResponse) {
        try {
            // fjern eventuelt “Response:”-prefix
            String json = removeResponsePrefix(jsonResponse);

            // deserialiser JSON til DTO
            EnturResponse dto = gson.fromJson(json, EnturResponse.class);

            return dto;
        }
        catch (JsonSyntaxException e) {
            System.err.println("Error with deserialization " + e.getMessage());
        }
        catch (Exception e){
            System.err.println("Error regarding request to api " + e.getMessage());
        }

        return null;
    }

}
