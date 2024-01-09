package com.akilisha.espresso.plugin.router;

import com.akilisha.espresso.api.plugin.IRouterHandle;
import com.akilisha.espresso.api.router.IRouter;
import com.akilisha.espresso.plugin.router.handler.BeforeAllHandler;
import com.akilisha.espresso.plugin.router.handler.RequestHandler;

public class RouterHandleDemo implements IRouterHandle {

    String contextPath = "/demos";

    @Override
    public String contextPath() {
        return this.contextPath;
    }

    public void init(IRouter app) {

        app.all("/es", new BeforeAllHandler());

        // curl http://localhost:3000/en
        app.method("get", "/en", new RequestHandler("GET Hello World!"));

        // curl -X POST http://localhost:3000/en
        app.method("post", "/en", new RequestHandler("POST Hello World!"));

        // curl -X PUT http://localhost:3000/en
        app.method("put", "/en", new RequestHandler("PUT Hello World!"));

        // curl -X DELETE http://localhost:3000/en
        app.method("delete", "/en", new RequestHandler("DEL Hello World!"));

        // curl http://localhost:3000/es
        app.get("/es", new RequestHandler("GET Hola Mundo!"));

        //  curl -X POST http://localhost:3000/es
        app.post("/es", new RequestHandler("POST Hola Mundo!"));

        // curl -X PUT http://localhost:3000/es
        app.put("/es", new RequestHandler("PUT Hola Mundo!"));

        // curl -X DELETE http://localhost:3000/en
        app.delete("/es", new RequestHandler("DEL Hola Mundo!"));

        // curl http://localhost:3000/es/10/10/2023
        app.get("/es/:day/:month/:year", (req, res, next) -> {
            String msg = String.format("%s/%s/%s", req.param("day"), req.param("month"), req.param("year"));
            res.send(msg);
        });
    }
}
