package works.hop.presso.jett.routable;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.request.ReqMethod;
import works.hop.presso.api.routeable.IMatched;
import works.hop.presso.api.routeable.IRoutable;

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
