package works.hop.presso.demo;

import works.hop.presso.demo.middleware.*;
import works.hop.presso.jett.Espresso;

public class Main {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.json());
        app.use(Espresso.text());

        var manage = app.route("/manage");

        manage.post("/question", new PostQuestion());

        manage.get("/:id", new FetchQuestion());

        manage.get("/:id/choice", new FetchQuestionChoices());

        manage.get("/:id/clue", new FetchQuestionClues());

        manage.get("/author/:name", new FetchQuestions());

        manage.put("/choice/:id", new UpdateChoice());

        manage.put("/clue/:id", new UpdateClue());

        manage.put("/:id", new UpdateQuestion());

        manage.delete("/:id", new DeleteQuestion());

        app.listen(3030);
    }
}