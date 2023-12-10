package works.hop.presso.extn;

import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.jett.Espresso;

public class Main {

    public static void main(String[] args) {
        IApplication app = Espresso.express();
        app.use("/", IStaticOptionsBuilder.newBuilder()
                .baseDirectory("www")
                .build());
        app.get("/hello", (req, res, next) -> res.send("Hello World!"));

        int port = 9080;
        app.listen("localhost", port, System.out::println);
    }
}