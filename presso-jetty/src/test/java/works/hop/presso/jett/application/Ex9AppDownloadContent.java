package works.hop.presso.jett.application;

import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.jett.Espresso;

public class Ex9AppDownloadContent {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use("/", IStaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());

        app.get("/download", (req, res, next) -> {
            res.download("C:\\Projects\\java\\nio-tcp-game\\presso-jetty\\view", "index.html");
        });

        app.get("/stream", (req, res, next) -> {
            res.sendFile("C:\\Projects\\java\\nio-tcp-game\\presso-jetty\\view\\index.html");
        });

        app.listen(3000);
    }
}
