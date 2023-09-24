package works.hop.presso.jett.application;

import java.nio.charset.Charset;

import static works.hop.presso.jett.Espresso.express;

public class Ex14ExceptionHandling {

    public static void main(String[] args) {
        var app = express();

        app.use((error, req, res, next) -> {
            if (next.errorCode() != null && next.errorCode().equals("404")) {
                res.end(error.getMessage(), Charset.defaultCharset().name());
                next.setHandled(true);
            }
        });

        app.get("/", (req, res, next) -> {
            throw new RuntimeException("Throwing generic exceptions like you don't care");
        });

        app.get("/404", (req, res, next) -> {
            next.error("404", new RuntimeException("Throwing 404 exceptions like you don't care"));
        });

        app.listen(3000);
    }
}
