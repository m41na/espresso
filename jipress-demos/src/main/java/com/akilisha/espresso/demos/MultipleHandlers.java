package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.jett.Espresso;

public class MultipleHandlers {

    public static void main(String[] args) {

        var app = Espresso.express();

        IMiddleware cb0 = (req, res, next) -> {
            System.out.println("CB0");
            next.ok();
        };

        IMiddleware cb1 = (req, res, next) -> {
            System.out.println("CB1");
            next.ok();
        };

        IMiddleware cb2 = (req, res, next) -> {
            System.out.println("CB2");
            next.error(new RuntimeException("cb2 is experiencing turbulence"));
            res.send("Interrupted from CB2!");
        };

        IMiddleware cb3 = (req, res, next) -> {
            System.out.println("CB3");
            res.send("Hello from CB3!");
        };

        // curl http://localhost:3000/example/c
        app.get("/example/c", cb0, cb1, /*cb2,*/ cb3);

        // curl http://localhost:3000/example/a
        app.get("/example/a", cb0, cb1, cb2, cb3);

        app.listen(3000);
    }
}
