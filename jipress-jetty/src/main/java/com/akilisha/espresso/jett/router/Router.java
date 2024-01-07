package com.akilisha.espresso.jett.router;

import com.akilisha.espresso.jett.routable.Routable;
import lombok.Getter;
import lombok.Setter;
import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.request.ReqMethod;
import com.akilisha.espresso.api.router.IRouter;

@Getter
@Setter
public class Router extends Routable implements IRouter {

    String basePath;

    @Override
    public void all(String path, IMiddleware... middlewares) {
        this.store(path, middlewares);
    }

    @Override
    public void method(String name, String path, IMiddleware... middlewares) {
        this.store(ReqMethod.valueOf(name.toUpperCase()), path, middlewares);
    }

    @Override
    public void get(String path, IMiddleware... middlewares) {
        method("GET", path, middlewares);
    }

    @Override
    public void post(String path, IMiddleware... middlewares) {
        method("POST", path, middlewares);
    }

    @Override
    public void put(String path, IMiddleware... middlewares) {
        method("PUT", path, middlewares);
    }

    @Override
    public void delete(String path, IMiddleware... middlewares) {
        method("DELETE", path, middlewares);
    }
}
