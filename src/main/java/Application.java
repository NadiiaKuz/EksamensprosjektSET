import io.javalin.Javalin;
import io.javalin.vue.VueComponent;

import io.javalin.http.staticfiles.Location;
import org.gruppe4.controllers.GraphQLController;
import org.gruppe4.controllers.UsersController;
import org.gruppe4.database.MySQLDatabase;
import org.gruppe4.database.MySQLDatabaseException;
import org.gruppe4.repository.GraphQLRepository;
import org.gruppe4.repository.UsersRepository;
import org.gruppe4.services.UsersService;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.enableWebjars();
            javalinConfig.staticFiles.add("/static", Location.CLASSPATH); //For å kunne hente ressurser fra static
            javalinConfig.vue.vueInstanceNameInJs = "app";
        }).start();

        // Setter opp controller
        GraphQLRepository graphQLRepository = new GraphQLRepository();
        GraphQLController graphQLController = new GraphQLController(graphQLRepository);

        // Laster inn innstillinger fra dbConfig.properties fil
        Properties properties = new Properties();
        try (FileInputStream file = new FileInputStream("dbConfig.properties")) {
            properties.load(file);
        } catch (IOException e) {
            System.err.println("Could not load dbConfig.properties: " + e.getMessage());
            return;
        }

        // Henter data fra en fil
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        // Lager kobling
        MySQLDatabase dataBase = new MySQLDatabase(url, user, password);
        Connection conn = null;

        try {
            conn = dataBase.startDB();
            System.out.println("Connected successful");
        } catch (MySQLDatabaseException e) {
            System.err.println("DB connection failed: " + e.getMessage());
        }

        // Manuell avhengighetsinjeksjon (Dependency Injection):
        // Opprett repositorier, tjenester og kontrollerinstanser,
        // send avhengigheter gjennom konstruktører.
        UsersRepository usersRepository = new UsersRepository(conn);
        UsersService usersService = new UsersService(usersRepository);
        UsersController usersController = new UsersController(usersService);

        // Oppsett av Users API-ruter
        app.get("/api/users/{email}", context -> usersController.getUserByEmail(context));
        app.post("/api/users", context -> usersController.createUser(context));

        app.get("/", new VueComponent("search-page"));
        app.get("/create-user", new VueComponent("create-user"));

        // metode for søk av ruter (bruker 'method reference')
        app.post("/api/search", graphQLController::getTransportRoutes);
    }
}
