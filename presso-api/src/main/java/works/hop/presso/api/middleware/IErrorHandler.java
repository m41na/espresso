package works.hop.presso.api.middleware;

import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

public interface IErrorHandler extends IMiddleware {

    void handle(Exception error, IRequest req, IResponse res, INext next);

    default Boolean isHandled() {
        return false;
    }

    default void handle(IRequest req, IResponse res, INext next) {
        this.handle(null, req, res, next);
    }
}
