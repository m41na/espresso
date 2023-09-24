package works.hop.presso.jett.application;

import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.servable.StaticOptionsBuilder;
import works.hop.presso.jett.Espresso;

import java.util.Map;

public class Ex8AppStaticContent {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.set(AppSettings.Setting.TEMPLATES_DIR.property, "presso-jetty/view");
        app.set(AppSettings.Setting.VIEW_ENGINE.property, "mvel");
        app.set(AppSettings.Setting.TEMPLATES_EXT.property, ".mvel");
        app.use(StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());
        app.use("/home", StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());

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
