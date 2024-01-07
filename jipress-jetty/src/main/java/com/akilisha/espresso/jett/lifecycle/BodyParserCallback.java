package com.akilisha.espresso.jett.lifecycle;

import com.akilisha.espresso.jett.content.BodyParserPlugins;
import com.akilisha.espresso.jett.content.BodyParsersCache;
import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.api.content.IBodyParser;
import com.akilisha.espresso.api.plugin.IBodyParserPlugin;
import com.akilisha.espresso.api.plugin.IPluginCallback;

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
