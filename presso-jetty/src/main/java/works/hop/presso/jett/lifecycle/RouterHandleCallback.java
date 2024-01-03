package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.api.plugin.IRouterHandle;
import works.hop.presso.api.plugin.IRouterHandlePlugin;
import works.hop.presso.api.router.IRouter;
import works.hop.presso.jett.router.RouterHandlePlugins;

import java.util.ServiceLoader;

@Slf4j
public class RouterHandleCallback implements IPluginCallback {

    private IRouterHandlePlugin plugins;

    public void reloadPlugins(IApplication app) {
        this.plugins = new RouterHandlePlugins(ServiceLoader.load(IRouterHandle.class));
        //TODO: figure out how to refresh static cache inside Factory class
    }

    public void loadPlugins(IApplication app) {
        this.plugins = new RouterHandlePlugins(ServiceLoader.load(IRouterHandle.class));
        this.plugins.loader().forEach(plugin -> {
            IRouter router = app.route(plugin.contextPath());
            plugin.init(router);
        });
    }
}
