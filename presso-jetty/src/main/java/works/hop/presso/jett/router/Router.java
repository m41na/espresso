package works.hop.presso.jett.router;

import lombok.Getter;
import lombok.Setter;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.request.ReqMethod;
import works.hop.presso.api.router.IRouter;
import works.hop.presso.jett.routable.Routable;

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
