package works.hop.presso.demo.middleware;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.demo.repo.Update;

import java.util.Map;
import java.util.UUID;

public class UpdateQuestion implements IMiddleware {

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        try {
            UUID id = req.param("id", UUID::fromString);
            String question = req.body(String::new);
            Update.update("update tbl_questions set content = ? where id = ?::uuid", new Object[]{question, id}, (th, result) -> {
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
