package works.hop.presso.jett.application;

import works.hop.presso.jett.content.PlainTextBodyParser;

import static works.hop.presso.jett.Espresso.express;

public class Ex17AppCustomBodyConverter {

    public static void main(String[] args) {
        var app = express();
        app.use(new PlainTextBodyParser());

        var string = app.route("/string");

        string.post("/", (req, res, next) -> {
            String plainText = req.body(String::new);
            res.send(plainText);
        });

        app.listen(3000);
    }
}
