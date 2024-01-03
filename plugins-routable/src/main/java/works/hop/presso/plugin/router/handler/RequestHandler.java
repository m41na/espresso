package works.hop.presso.plugin.router.handler;

import lombok.RequiredArgsConstructor;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

@RequiredArgsConstructor
public class RequestHandler implements IMiddleware {

    final String message;

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        res.send(this.message);
    }
}
