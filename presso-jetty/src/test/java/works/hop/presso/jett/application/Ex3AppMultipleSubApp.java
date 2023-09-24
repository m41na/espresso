package works.hop.presso.jett.application;

import works.hop.presso.jett.Espresso;

public class Ex3AppMultipleSubApp {

    public static void main(String[] args) {
        var app = Espresso.express();

        var admin = Espresso.express();
        admin.get("/", (req, res, next) -> {
            System.out.println(admin.mountPath());  // [ '/adm*n', '/manager' ]
            res.send("Admin Homepage");
        });

        var secret = Espresso.express();
        secret.get("/", (req, res, next) -> {
            System.out.println(secret.mountPath()); // '/secr*t'
            res.send("Admin Secret");
        });

        admin.use("/secr*t", secret);
        app.use(new String[]{"/adm*n", "/manager"}, admin);

        app.get("/", (req, res, next) -> {
            System.out.println(app.mountPath()); // '/'
            res.send("App Home");
        });

        app.listen(3000);
    }
}
