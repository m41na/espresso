package works.hop.presso.jett.application;

import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.middleware.IMiddleware;

import static works.hop.presso.jett.Espresso.express;

public class Ex15RequestAppFunction {

    static IMiddleware middleware = (req, res, next) -> {
        res.send("The views directory is " + req.app().get("templates dir"));
        //expect - The views directory is my-view-folder
    };

    public static void main(String[] args) {
        var app = express();

        app.set(AppSettings.Setting.TEMPLATES_DIR.property, "my-view-folder");
        app.get("/", middleware);

        var greet = app.route("/greet");

        greet.get("/jp", (req, res, next) -> {
            System.out.println(req.baseUrl()); // /greet
            res.send("Konichiwa!");
        });

        var greet2 = express();

        greet2.get("/swa", (req, res, next) -> {
            System.out.println(req.baseUrl()); // /greet2
            res.send("Shikamoo!");
        });

        app.use("/greet2", greet2);

        app.listen(3000);
    }
}
