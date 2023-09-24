package works.hop.presso.demo.middleware;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.demo.repo.Select;

import java.util.Map;
import java.util.UUID;

public class FetchQuestionClues implements IMiddleware {

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        UUID fk = req.param("id", UUID::fromString);

        Select.select("select tcl.id, tcl.clue, tch.choice from tbl_clues tcl inner join tbl_choices tch on tcl.choice_fk = tch.id where tch.question_fk = ?", new Object[]{fk}, (th, result) -> {
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
