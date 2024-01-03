package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.api.plugin.IViewEnginePlugin;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.jett.view.ViewEngineFactory;
import works.hop.presso.jett.view.ViewEnginePlugins;

import java.util.ServiceLoader;

@Slf4j
public class ViewEnginesCallback implements IPluginCallback {

    private IViewEnginePlugin plugins;

    @Override
    public void reloadPlugins(IApplication app) {
        this.plugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class));
        //TODO: figure out how to refresh static cache inside Factory class
    }

    @Override
    public void loadPlugins(IApplication app) {
        this.plugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class));
        this.plugins.loader().forEach(engine -> ViewEngineFactory.register(engine.name(), engine));
    }
}
