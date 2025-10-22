import io.javalin.Javalin;
import io.javalin.vue.VueComponent;

//For å få tilgang til static-filer:
import io.javalin.http.staticfiles.Location;
import org.gruppe4.controllers.UsersController;
import org.gruppe4.repository.UsersRepository;
import org.gruppe4.services.UsersService;

public class Application {
    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.enableWebjars();
            javalinConfig.staticFiles.add("/static", Location.CLASSPATH); //For å kunne hente ressurser fra static
            javalinConfig.vue.vueInstanceNameInJs = "app";
        }).start();

        UsersRepository usersRepository = new UsersRepository();
        UsersService usersService = new UsersService(usersRepository);
        UsersController usersController = new UsersController(usersService);

        app.get("/api/users/{name}", context -> usersController.getUserByName(context));
        app.post("/api/users", context -> usersController.createUser(context));

        app.get("/", new VueComponent("search-page"));
    }
}
