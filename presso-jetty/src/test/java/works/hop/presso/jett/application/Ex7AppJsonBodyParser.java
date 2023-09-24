package works.hop.presso.jett.application;

import works.hop.presso.api.cookie.CookieOptions;
import works.hop.presso.api.cookie.CookieOptionsBuilder;
import works.hop.presso.jett.Espresso;

import java.util.Map;

public class Ex7AppJsonBodyParser {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.json());

        app.use((req, res, next) -> {
            res.locals().put("name", "Jimmy");
            next.ok();
        });

        app.get("/blog/:id", (req, res, next) -> {
            String name = (String) res.locals().get("name");
            CookieOptions cookieOptions = CookieOptionsBuilder.newBuilder().path("/admin").secure(true).build();
            res.cookie("name", name, cookieOptions);
            res.clearCookie("name");
            res.send(String.format("user id: %s, name: %s", req.params().get("id"), name));
        });

        app.route("/entry").get("/:key/:value", (req, res, next) -> {
            String key = req.param("key");
            String value = req.param("value");
            res.json(Map.of("key", key, "value", value));
        });

        app.listen(3000);
    }
}
