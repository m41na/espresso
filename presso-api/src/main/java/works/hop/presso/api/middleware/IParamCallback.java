package works.hop.presso.api.middleware;

import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

public interface IParamCallback extends IMiddleware {

    void handle(IRequest req, IResponse res, INext next, Object paramValue);

    @Override
    default void handle(IRequest req, IResponse res, INext next) {
        this.handle(req, res, next, null);
    }
}
