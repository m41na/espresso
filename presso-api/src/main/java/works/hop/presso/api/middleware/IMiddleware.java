package works.hop.presso.api.middleware;

import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

public interface IMiddleware {

    default void handle(IRequest req, IResponse res) {
        handle(req, res, null);
    }

    void handle(IRequest req, IResponse res, INext next);
}
