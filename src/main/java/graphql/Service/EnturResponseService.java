package graphql.Service;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import graphql.client.GraphQLClient;
import graphql.dto.EnturResponse;
import graphql.query.GraphQLQuery;

public class EnturResponseService {

    private final GraphQLClient client;
    private final GraphQLQuery queryBuilder;
    private final Gson gson;

    public EnturResponseService() {
        this.client = new GraphQLClient();
        this.queryBuilder = new GraphQLQuery();
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


    public EnturResponse getEnturResponse(int fromStopId, int toStopId) {
        try {
            String body = queryBuilder.getQuery(fromStopId, toStopId);

            String jsonResponse = client.sendGraphQLRequest(body);

            String json = removeResponsePrefix(jsonResponse);

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


