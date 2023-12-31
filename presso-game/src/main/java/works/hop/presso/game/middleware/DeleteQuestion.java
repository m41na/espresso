package works.hop.presso.game.middleware;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.game.repo.Update;

import java.util.Map;
import java.util.UUID;

public class DeleteQuestion implements IMiddleware {

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        try {
            UUID id = req.param("id", UUID::fromString);
            Update.update("delete from tbl_questions where id = ?::uuid",
                    new Object[]{id},
                    (th, result) -> {
                        if (th != null) {
                            res.status(500);
                            res.json(Map.of("error", th.getCause().getMessage()));
                        } else {
                            res.sendStatus(201);
                        }
                    });

        } catch (Exception e) {
            res.status(500);
            res.json(Map.of("error", e.getMessage()));
        }
    }
}
