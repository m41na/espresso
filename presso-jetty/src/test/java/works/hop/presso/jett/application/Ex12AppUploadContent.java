package works.hop.presso.jett.application;

import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.jett.config.ConfigMap;
import works.hop.presso.jett.config.MultipartConfig;

import static works.hop.presso.jett.Espresso.express;
import static works.hop.presso.jett.Espresso.multipart;

public class Ex12AppUploadContent {

    public static void main(String[] args) {
        var app = express();
        app.properties("app-default.yaml");
        app.use("/upload", IStaticOptionsBuilder.newBuilder()
                .baseDirectory("presso-jetty/view")
                .welcomeFiles("upload.html")
                .listDirectories(false)
                .acceptRanges(true)
                .build());
        app.use(multipart(
                System.getProperty("java.io.tmpdir"),
                ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getMaxFileSize(),
                ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getMaxRequestSize(),
                ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getFileSizeThreshold()));

        app.get("/", (req, res, next) -> {
            res.send("Hello, trying to upload");
        });

        app.post("/some", (req, res, next) -> {
            req.upload(); //this should upload content
            res.sendStatus(201);
        });

        app.listen(3000);
    }
}
