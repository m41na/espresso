package works.hop.presso.jett.application;

import static works.hop.presso.jett.Espresso.express;

public class Ex1Application {

    public static void main(String[] args) {
        var app = express();

        app.get("/", (req, res, next) -> res.send("hello world example 1"));
        app.locals().put("title", "My App");

        app.listen(3000);
    }
}
