package works.hop.presso.jett.application;

import works.hop.presso.jett.Espresso;

public class Ex6AppPathParams {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.get("/user/:id", (req, res, next) -> res.send("hello world with path params"));
        app.param("id", (req, res, next, id) -> {
            System.out.println("CALLED ONLY ONCE");
            next.ok();
        });

        app.listen(3000);
    }
}
