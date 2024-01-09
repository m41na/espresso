package com.akilisha.espresso.jett.lifecycle;

import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.api.plugin.IPluginCallback;
import com.akilisha.espresso.api.plugin.IRouterHandle;
import com.akilisha.espresso.api.plugin.IRouterHandlePlugin;
import com.akilisha.espresso.api.router.IRouter;
import com.akilisha.espresso.jett.router.RouterHandlePlugins;
import lombok.extern.slf4j.Slf4j;

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
