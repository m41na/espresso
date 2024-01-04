package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.api.plugin.IViewEnginePlugin;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.jett.view.ViewEnginePlugins;
import works.hop.presso.jett.view.ViewEnginesCache;

import java.util.ServiceLoader;

@Slf4j
public class ViewEnginesCallback implements IPluginCallback {

    @Override
    public void reloadPlugins(IApplication app) {
        log.info("Start reloading plugins");
        ViewEnginesCache.deregister();
        this.loadPlugins(app);
    }

    @Override
    public void loadPlugins(IApplication app) {
        IViewEnginePlugin plugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class));
        plugins.loader().forEach(engine -> ViewEnginesCache.register(engine.name(), engine));
    }
}
