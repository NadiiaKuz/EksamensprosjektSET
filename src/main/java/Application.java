import io.javalin.Javalin;
import io.javalin.vue.VueComponent;

//For å få tilgang til static-filer:
import io.javalin.http.staticfiles.Location;

public class Application {
    public static void main(String[] args) {

        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.enableWebjars();
            javalinConfig.staticFiles.add("/static", Location.CLASSPATH); //For å kunne hente ressurser fra static
            javalinConfig.vue.vueInstanceNameInJs = "app";
        }).start();

        app.get("/", new VueComponent("search-page"));
    }
}
