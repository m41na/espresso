package works.hop.presso.jett.application;

import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.servable.IStaticOptionsBuilder;

import java.util.Map;

import static works.hop.presso.jett.Espresso.express;

public class Ex8AppStaticContent {

    public static void main(String[] args) {
        var app = express();
        app.set(AppSettings.Setting.TEMPLATES_DIR.property, "presso-jetty/view");
        app.set(AppSettings.Setting.VIEW_ENGINE.property, "mvel");
        app.set(AppSettings.Setting.TEMPLATES_EXT.property, ".mvel");
//        app.engine(new MvelViewEngine("presso-jetty/view"), ".mvel");
        app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());
        app.use("/home", IStaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());

        app.use((req, res, next) -> {
            res.locals().put("name", "Jimmy");
            next.ok();
        });

        app.get("/home/1", (req, res, next) -> {
            String name = (String) res.locals().get("name");
            res.render("home", Map.of("name", name), res::send);
        });

        app.listen(3000);
    }
}
