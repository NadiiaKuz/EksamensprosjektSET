import com.google.gson.Gson;

public class testGson {

    static class Person {
        String navn;
        int alder;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        String json = "{\"navn\":\"Abikar\",\"alder\":20}";
        Person person = gson.fromJson(json, Person.class);
        System.out.println(person.navn + " er " + person.alder + " år gammel ");

    }

}
