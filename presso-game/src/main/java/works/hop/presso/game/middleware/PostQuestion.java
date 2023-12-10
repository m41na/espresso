package works.hop.presso.game.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.game.model.QChoice;
import works.hop.presso.game.model.QClue;
import works.hop.presso.game.model.Question;
import works.hop.presso.game.repo.Update;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * valid query payload
 * {"content": "1 + 1", "choices": [{"ordinal": 1, "choice": "1", "correct": false}, {"ordinal": 2, "choice": "2", "correct": true}, {"ordinal": 3, "choice": "3", "correct": false}, {"ordinal": 4, "choice": "4", "correct": false}], "clues": [{"ordinal": 4, "clue": "no quads"}, {"ordinal": 3, "clue": "triangles do not fit"}, {"ordinal": 1, "clue": "not singular"}, {"ordinal": 2, "clue": "double vision"}], "postedBy": "tester"}'
 */
public class PostQuestion implements IMiddleware {

    ObjectMapper objectMapper = new ObjectMapper();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        try {

            Question question = objectMapper.convertValue(req.body(), Question.class);
            Set<ConstraintViolation<Question>> violations = validator.validate(question);
            if (violations.isEmpty()) {

                List<Update.UnitOfWork> op1 = List.of(new Update.UnitOfWork(
                        "insert into tbl_questions (content, posted_by) values (?, ?) on conflict (content) do nothing",
                        new Object[]{question.getContent(), question.getPostedBy()}));

                List<Update.UnitOfWork> op2 = Arrays.stream(question.getChoices()).map(ch -> new Update.UnitOfWork(
                        "insert into tbl_choices (choice, correct_choice, question_fk) values " +
                                "(?, ?, (select id from tbl_questions where content = ?)) " +
                                "on conflict (choice, question_fk) do nothing ",
                        new Object[]{ch.getChoice(), ch.getCorrect(), question.getContent()})).toList();

                List<Update.UnitOfWork> op3 = IntStream.range(0, question.getClues().length).mapToObj(i -> {
                    QClue clue = question.getClues()[i];
                    QChoice choice = question.getChoices()[i];
                    return new Update.UnitOfWork(
                            "insert into tbl_clues (clue, choice_fk) values " +
                                    "(?, (select id from tbl_choices where choice = ?)) " +
                                    "on conflict (clue, choice_fk) do nothing",
                            new Object[]{clue.getClue(), choice.getChoice()});
                }).toList();

                Update.UnitOfWork[] works = Stream.of(op1, op2, op3).flatMap(Collection::stream).toArray(Update.UnitOfWork[]::new);

                Update.update(works, (th, result) -> {
                    if (th != null) {
                        res.status(500);
                        res.json(Map.of("error", th.getCause().getMessage()));
                    } else {
                        res.sendStatus(201);
                    }
                });
            } else {
                res.status(400);
                res.json(violations.stream().map(v -> new String[]{v.getPropertyPath().toString(), v.getMessage()}).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            res.status(500);
            res.json(Map.of("error", e.getMessage()));
        }
    }
}
