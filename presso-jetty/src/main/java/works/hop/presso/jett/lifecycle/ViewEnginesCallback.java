package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.api.plugin.IViewEnginePlugin;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.jett.view.ViewEngineFactory;
import works.hop.presso.jett.view.ViewEnginePlugins;

import java.util.ServiceLoader;

@Slf4j
public class ViewEnginesCallback implements IPluginCallback<ViewEngineFactory> {

    private IViewEnginePlugin plugins;

    public void reloadPlugins(ViewEngineFactory factory) {
        this.plugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class));
        //TODO: figure out how to refresh static cache inside Factory class
    }

    public void loadPlugins(ViewEngineFactory factory) {
        this.plugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class));
        this.plugins.loader().forEach(engine -> ViewEngineFactory.register(engine.name(), engine));
    }
}
