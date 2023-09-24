package works.hop.presso.jett.application;

import works.hop.presso.jett.Espresso;

public class Ex4AppOnMount {

    public static void main(String[] args) {
        var app = Espresso.express();

        var admin = Espresso.express();
        admin.on("mount", (parent) -> {
            System.out.println("Admin Mounted");
            System.out.printf("parent mountPath - /%s\n", parent.mountPath()); // refers to the parent app
        });

        admin.get("/", (req, res, next) -> {
            res.send("Admin Homepage");
        });

        app.use("/admin", admin);

        app.listen(3000);
    }
}
