package works.hop.presso.demo.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.demo.model.QChoice;
import works.hop.presso.demo.repo.Update;

import java.util.Map;
import java.util.UUID;

public class UpdateChoice implements IMiddleware {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        try {
            UUID id = req.param("id", UUID::fromString);
            QChoice choice = objectMapper.convertValue(req.body(), QChoice.class);
            Update.update("update tbl_choices set choice = ?, correct_choice = ? where id = ?::uuid",
                    new Object[]{choice.getChoice(), choice.getCorrect(), id},
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
