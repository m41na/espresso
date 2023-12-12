package works.hop.presso.demos;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.request.ReqCookies;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.jett.Espresso;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ContentHandlers {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("presso-demos/www").welcomeFiles("content-handlers.html").build());
        app.use(Espresso.multipart(Paths.get(System.getProperty("java.io.tmpdir"), "upload").toString()));
        app.use(Espresso.urlEncoded());
        app.use(Espresso.json());
        app.use(Espresso.raw());

        // curl -X POST http://localhost:3000/json -H "Content-Type: application/json"
        // -d '{"name": "Jimmy", "age": 20}'
        app.post("/json", (req, res, next) -> {
            Object body = req.body();
            ReqCookies cookies = req.cookies();
            res.json(List.of(body, cookies));
        });

        // curl -X POST http://localhost:3000/multipart -H "Content-Type: multipart/form-data"
        // -F name=sample-file
        // -F content=@/c/Projects/java/espresso/presso-demos/demo/multipart.txt
        app.post("/multipart", (req, res, next) -> {
            Object result = req.body();
            log.info("multipart upload - {}", result);
            res.sendStatus(201);
        });

        // curl -X POST http://localhost:3000/formencoded -H "Content-Type: application/x-www-form-urlencoded"
        // -d "param1=value1&param2=value2"
        app.post("/formencoded", (req, res, next) -> {
            Object form = req.body();
            res.send(form.toString());
        });

        // curl -X POST http://localhost:3000/download -H "Content-Type: application/x-www-form-urlencoded"
        // -d "folder=/c/Projects/java/espresso/presso-demos/demo&fileName=download.txt"
        app.post("/download", (req, res, next) -> {
            Map<String, Object> options = req.body();
            res.download(
                    ((ArrayList<?>) options.get("folder")).get(0).toString(),
                    ((ArrayList<?>) options.get("fileName")).get(0).toString(),
                    null,
                    error -> {
                        if (error == null) {
                            res.end();
                        } else {
                            res.status(500);
                            res.send(error.getMessage());
                        }
                    });

        });

        app.listen(3000);
    }
}
