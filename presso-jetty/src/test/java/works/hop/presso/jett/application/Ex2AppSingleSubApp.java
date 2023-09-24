package works.hop.presso.jett.application;

import static works.hop.presso.jett.Espresso.express;

public class Ex2AppSingleSubApp {

    public static void main(String[] args) {
        var app = express();
        var admin = express();

        admin.get("/", (req, res, next) -> {
            System.out.println(admin.mountPath());
            res.send("hello example 2 from admin");
        });

        app.use("/admin", admin);
        app.listen(3000);
    }
}
