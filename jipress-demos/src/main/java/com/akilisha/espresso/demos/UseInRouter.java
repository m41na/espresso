package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.jett.Espresso;

import java.util.Date;

public class UseInRouter {

    public static void main(String[] args) {
        var app = Espresso.express();

        birdsRoute(app, "/birds");

        app.get("/other", (req, res, next) -> {
            System.out.println("HANDLING OTHER CTX");
            res.send("Other handled");
        });

        app.get("/jungle/:animal", (req, res, next) -> {
            System.out.println("HANDLING JUNGLE ANIMAL");
            res.send("Jungle animal handled");
        });

        gatorRoute(app, "/");

        app.listen(3000);
    }

    public static void birdsRoute(IApplication app, String path) {
        IApplication birds = Espresso.express();

        birds.use((req, res, next) -> {
            System.out.printf("Birds Time: %s%n", new Date());
            next.ok();
        });

        // define the 'home page' route
        birds.get("/", (req, res, next) -> {
            res.send("Birds home page");
        });

        // define the 'about' route
        birds.get("/about", (req, res, next) -> {
            res.send("About birds");
        });

        app.use(path, birds);
    }

    public static void gatorRoute(IApplication app, String path) {
        IApplication gator = Espresso.express();

        gator.use((req, res, next) -> {
            System.out.printf("Gator Time: %s%n", new Date());
            next.ok();
        });

        // define the 'home page' route
        gator.get("/", (req, res, next) -> {
            res.send("Gators home page");
        });

        // define the 'about' route
        gator.get("/about", (req, res, next) -> {
            res.send("About gators");
        });

        app.use(path, gator);
    }
}
