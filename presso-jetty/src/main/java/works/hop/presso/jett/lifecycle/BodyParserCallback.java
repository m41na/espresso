package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.plugin.IBodyParserPlugin;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.jett.content.BodyParsersCache;
import works.hop.presso.jett.content.BodyParserPlugins;

import java.util.ServiceLoader;

@Slf4j
public class BodyParserCallback implements IPluginCallback {

    @Override
    public void reloadPlugins(IApplication app) {
        log.info("Start reloading plugins");
        BodyParsersCache.deregister();
        this.loadPlugins(app);
    }

    @Override
    public void loadPlugins(IApplication app) {
        log.info("Start loading plugins");
        IBodyParserPlugin plugins = new BodyParserPlugins(ServiceLoader.load(IBodyParser.class));
        plugins.loader().forEach(parser -> BodyParsersCache.register(parser.contentType(), parser));
    }
}
