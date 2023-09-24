package works.hop.presso.demo.middleware;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.demo.repo.Select;

import java.util.Map;
import java.util.UUID;

public class FetchQuestion implements IMiddleware {

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        UUID id = req.param("id", UUID::fromString);

        Select.select("select * from tbl_questions where id = ?::uuid", new Object[]{id}, (th, result) -> {
            if (th != null) {
                res.status(500);
                res.json(Map.of("error", th.getCause().getMessage()));
            } else {
                res.status(200);
                res.json(result);
            }
        });
    }
}
