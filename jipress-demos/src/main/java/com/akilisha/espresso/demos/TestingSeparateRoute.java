package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.view.IViewEngine;
import com.akilisha.espresso.jett.Espresso;

import java.util.Map;

class TestingSeparateRoute {

    public static void main(String[] args) {
        var app = Espresso.express();

        var route = app.route("/book");
        // curl http://localhost:3000/book/
        route.get("/", (req, res, next) -> res.send("Get any book"));
        // curl -X POST http://localhost:3000/book/
        route.post("/", (req, res, next) -> res.send("Add a book"));
        // curl -X PUT http://localhost:3000/book/
        route.put("/", (req, res, next) -> res.send("Update the book"));
        // curl -X DELETE http://localhost:3000/book/
        route.delete("/", (req, res, next) -> res.send("Remove the book"));
        app.engine(IViewEngine.MVEL, "jipress-demos/templates", ".mvel");

        // curl http://localhost:3000/
        app.get("/", (req, res, next) ->
                res.render("template", Map.of("name", "Michael")));

        app.listen(3000);
    }
}