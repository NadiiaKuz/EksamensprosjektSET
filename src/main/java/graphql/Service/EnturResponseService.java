package graphql.Service;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import graphql.client.GraphQLClient;
import graphql.dto.EnturResponse;
import graphql.query.GraphQLQuery;
import graphql.query.QueryObject;

public class EnturResponseService {

    private final GraphQLClient client;
    private final GraphQLQuery queryBuilder;
    private final Gson gson;

    public EnturResponseService() {
        this.client = new GraphQLClient();
        this.queryBuilder = new GraphQLQuery(null);
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
            // opprett QueryObject basert på parameterne
            QueryObject queryObject = new QueryObject(fromStopId, toStopId);

            // la GraphQLQuery automatisk finne riktig forespørsel
            String body = queryBuilder.getQueryBasedOnProvidedParameters(queryObject);

            // send forespørsel til Entur API
            String jsonResponse = client.sendGraphQLRequest(body);

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
