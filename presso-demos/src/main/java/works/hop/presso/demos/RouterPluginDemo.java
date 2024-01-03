package works.hop.presso.demos;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.jett.Espresso;

import java.nio.file.Paths;

@Slf4j
public class RouterPluginDemo {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("presso-demos/www").welcomeFiles("content-handlers.html").build());
        app.use(Espresso.multipart(Paths.get(System.getProperty("java.io.tmpdir"), "upload").toString()));
        app.listen(3000);
    }
}
