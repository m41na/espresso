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

    public void reloadPlugins(IApplication app) {
        log.warn("To reload router handle plugins, you need to restart the application");
    }

    public void loadPlugins(IApplication app) {
        IRouterHandlePlugin plugins = new RouterHandlePlugins(ServiceLoader.load(IRouterHandle.class));
        plugins.loader().forEach(plugin -> {
            IRouter router = app.route(plugin.contextPath());
            plugin.init(router);
        });
    }
}
