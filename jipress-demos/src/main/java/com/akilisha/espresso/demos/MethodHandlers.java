package com.akilisha.espresso.demos;

import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.servable.IStaticOptionsBuilder;
import com.akilisha.espresso.jett.Espresso;

@Slf4j
public class MethodHandlers {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.use("/static", IStaticOptionsBuilder.newBuilder()
                .baseDirectory("jipress-demos/www")
                .welcomeFiles("method-handlers.html")
                .acceptRanges(true)
                .listDirectories(false).build());

        app.all("/es", (req, res, next) -> res.send("ALL Holla Mundo!"));

        // curl http://localhost:3000/en
        app.method("get", "/en", (req, res, next) -> res.send("GET Hello World!"));

        // curl -X POST http://localhost:3000/en
        app.method("post", "/en", (req, res, next) -> res.send("POST Hello World!"));

        // curl -X PUT http://localhost:3000/en
        app.method("put", "/en", (req, res, next) -> res.send("PUT Hello World!"));

        // curl -X DELETE http://localhost:3000/en
        app.method("delete", "/en", (req, res, next) -> res.send("DEL Hello World!"));

        // curl http://localhost:3000/es
        app.get("/es", (req, res, next) -> res.send("GET Hola Mundo!"));

        //  curl -X POST http://localhost:3000/es
        app.post("/es", (req, res, next) -> res.send("POST Hola Mundo!"));

        // curl -X PUT http://localhost:3000/es
        app.put("/es", (req, res, next) -> res.send("PUT Hola Mundo!"));

        // curl -X DELETE http://localhost:3000/en
        app.delete("/es", (req, res, next) -> res.send("DEL Hola Mundo!"));

        // curl http://localhost:3000/es/10/10/2023
        app.get("/es/:day/:month/:year", (req, res, next) -> {
            String msg = String.format("%s/%s/%s", req.param("day"), req.param("month"), req.param("year"));
            res.send(msg);
        });

        app.listen(3000);
    }
}
