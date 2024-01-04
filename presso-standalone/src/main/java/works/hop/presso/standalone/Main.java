package works.hop.presso.standalone;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.plugin.Directories;
import works.hop.presso.api.plugin.DirectoryInfo;
import works.hop.presso.api.request.ReqCookies;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.cli.OptBuilder;
import works.hop.presso.cli.StartUp;
import works.hop.presso.jett.Espresso;

import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        final OptBuilder options = OptBuilder.newBuilder();
        StartUp props = StartUp.load(options, args);

        //configure plugin directories
        Directories.PLUGINS.put(DirectoryInfo.PLUGINS_HOME, props.getOptionValue(DirectoryInfo.PLUGINS_HOME.folder));
        Directories.PLUGINS.put(DirectoryInfo.ROUTER_HANDLES, props.getOptionValue(DirectoryInfo.ROUTER_HANDLES.folder));
        Directories.PLUGINS.put(DirectoryInfo.VIEW_ENGINES, props.getOptionValue(DirectoryInfo.VIEW_ENGINES.folder));
        Directories.PLUGINS.put(DirectoryInfo.BODY_PARSERS, props.getOptionValue(DirectoryInfo.BODY_PARSERS.folder));

        //get additional options
        int httpPort = props.getOrDefault("port", Integer::parseInt, 9080);
        String hostName = props.getOrDefault("host", "localhost");

        var app = Espresso.express();

        app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("presso-demos/www").welcomeFiles("content-handlers.html").build());
        app.use(Espresso.json());

        // curl -X POST http://localhost:3000/json -H "Content-Type: application/json"
        // -d '{"name": "Jimmy", "age": 20}'
        app.post("/json", (req, res, next) -> {
            Object body = req.body();
            ReqCookies cookies = req.cookies();
            res.json(List.of(body, cookies));
        });

        app.listen(hostName, httpPort, System.out::println);
    }
}
