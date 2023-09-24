package works.hop.presso.jett.application;

import works.hop.presso.jett.Espresso;

public class Ex5AppAllHandler {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.get("/", (req, res, next) -> res.send("hello world ex 5"));
        app.locals().put("title", "My App");

        var admin = Espresso.express();
        admin.get("/", (req, res, next) -> {
            System.out.println(admin.mountPath());  // [ '/adm*n', '/manager' ]
            res.send("All handler Homepage");
        });

        app.all("/secret", (req, res, next) -> {
            System.out.println("All methods for the secret section ...");
            next.ok(); // pass control to the next handler
        });

        app.get("/secret", (req, res, next) -> {
            System.out.println("get secret sections ...");
            res.send("GET was successful");
        });

        app.post("/secret", (req, res, next) -> {
            System.out.println("post secret sections ...");
            res.send("POST was successful");
        });

        app.use("/adm*n", admin);

        app.listen(3000);
    }
}
