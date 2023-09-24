package works.hop.presso.jett.application;

import works.hop.presso.jett.Espresso;

import java.util.Map;

import static works.hop.presso.jett.Espresso.json;
import static works.hop.presso.jett.Espresso.urlEncoded;

public class Ex10AppReadJsonOrFormData {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(json());
        app.use(urlEncoded());

        app.get("/json", (req, res, next) -> {
            Map<String, Object> json = Map.of("name", "Janie", "age", 23);
            System.out.println(json);
            res.json(json);
        });

        app.get("/json", (req, res, next) -> {
            Map<String, Object> json = Map.of("name", "Janie", "age", 23);
            System.out.println(json);
            res.json(json);
        });

        app.post("/json", (req, res, next) -> {
            Map<String, Object> json = req.body();
            System.out.println(json);
            res.json(json);
        });

        app.post("/form", (req, res, next) -> {
            Map<String, Object> json = req.body();
            System.out.println(json);
            res.json(json);
        });

        app.listen(3000);
    }
}
