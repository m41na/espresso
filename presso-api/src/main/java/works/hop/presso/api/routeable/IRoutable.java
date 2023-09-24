package works.hop.presso.api.routeable;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.request.ReqMethod;

public interface IRoutable {

    void store(ReqMethod method, String path, IMiddleware... handlers);

    IMatched select(ReqMethod method, String path);

    void store(String path, IMiddleware... handlers);

    boolean canRoute();
}
