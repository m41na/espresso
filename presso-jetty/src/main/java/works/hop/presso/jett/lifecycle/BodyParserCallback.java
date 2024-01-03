package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.plugin.IBodyParserPlugin;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.jett.content.BodyParserFactory;
import works.hop.presso.jett.content.BodyParserPlugins;

import java.util.ServiceLoader;

@Slf4j
public class BodyParserCallback implements IPluginCallback {

    private IBodyParserPlugin plugins;

    @Override
    public void reloadPlugins(IApplication app) {
        log.info("Start reloading plugins");
        this.plugins = new BodyParserPlugins(ServiceLoader.load(IBodyParser.class));
        //TODO: figure out how to refresh static cache inside Factory class
    }

    @Override
    public void loadPlugins(IApplication app) {
        log.info("Start loading plugins");
        this.plugins = new BodyParserPlugins(ServiceLoader.load(IBodyParser.class));
        this.plugins.loader().forEach(parser -> BodyParserFactory.register(parser.contentType(), parser));
    }
}
