package works.hop.presso.standalone;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.plugin.PluginsDir;
import works.hop.presso.api.request.ReqCookies;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.cli.OptBuilder;
import works.hop.presso.cli.StartUp;
import works.hop.presso.jett.Espresso;
import works.hop.presso.jett.plugin.DirLayout;

import java.util.List;

@Slf4j
public class StandaloneApp {

    public static void main(String[] args) {
        final OptBuilder options = OptBuilder.newBuilder();
        StartUp props = StartUp.load(options, args);

        //configure plugin directories
        DirLayout.PLUGINS.put(PluginsDir.PLUGINS_HOME_DIR, props.getOptionValue(PluginsDir.PLUGINS_HOME_DIR.folder));
        DirLayout.PLUGINS.put(PluginsDir.ROUTER_HANDLE_PLUGINS, props.getOptionValue(PluginsDir.ROUTER_HANDLE_PLUGINS.folder));
        DirLayout.PLUGINS.put(PluginsDir.VIEW_ENGINE_PLUGINS, props.getOptionValue(PluginsDir.VIEW_ENGINE_PLUGINS.folder));
        DirLayout.PLUGINS.put(PluginsDir.BODY_PARSER_PLUGINS, props.getOptionValue(PluginsDir.BODY_PARSER_PLUGINS.folder));

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
