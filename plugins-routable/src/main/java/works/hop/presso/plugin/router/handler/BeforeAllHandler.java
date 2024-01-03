package works.hop.presso.plugin.router.handler;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

public class BeforeAllHandler implements IMiddleware {
    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        res.send("ALL Holla Mundo!");
    }
}
