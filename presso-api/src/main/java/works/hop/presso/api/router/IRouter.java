package works.hop.presso.api.router;

import works.hop.presso.api.middleware.IMiddleware;

public interface IRouter {

    void all(String path, IMiddleware... middlewares);

    void method(String name, String path, IMiddleware... middlewares);

    void get(String path, IMiddleware... middlewares);

    void post(String path, IMiddleware... middlewares);

    void put(String path, IMiddleware... middlewares);

    void delete(String path, IMiddleware... middlewares);
}
