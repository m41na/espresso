package works.hop.presso.demos;

import works.hop.presso.jett.Espresso;

import java.nio.charset.Charset;
import java.util.List;

public class CustomErrorHandler {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.text());
        app.use((error, req, res, next) -> {
            if (next.errorCode().equals("1001")) {
                res.end("Custom Error Code 1001 handled", Charset.defaultCharset().name());
                next.setHandled(true);
            }
        });

        // curl localhost:3000 -> All clear
        // curl localhost:3000?err=throw -> Custom Error Code 1001 handled
        app.get("/", (req, res, next) -> {
            List<String> err = req.query("err", String::toString);
            if (err.contains("throw")) {
                next.error("1001", new RuntimeException("Get me out of here"));
            } else {
                res.send("All clear");
            }
        });

        app.listen(3000);
    }
}
