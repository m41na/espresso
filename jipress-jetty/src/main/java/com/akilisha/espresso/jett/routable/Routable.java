package com.akilisha.espresso.jett.routable;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.request.ReqMethod;
import com.akilisha.espresso.api.routeable.IMatched;
import com.akilisha.espresso.api.routeable.IRoutable;

public class Routable implements IRoutable {

    final IRoutable routes = new ByMethod();

    @Override
    public void store(ReqMethod method, String path, IMiddleware... handlers) {
        this.routes.store(method, path, handlers);
    }

    @Override
    public IMatched select(ReqMethod method, String path) {
        return this.routes.select(method, path);
    }

    @Override
    public void store(String path, IMiddleware... handlers) {
        this.routes.store(path, handlers);
    }

    @Override
    public boolean canRoute() {
        return this.routes.canRoute();
    }
}
