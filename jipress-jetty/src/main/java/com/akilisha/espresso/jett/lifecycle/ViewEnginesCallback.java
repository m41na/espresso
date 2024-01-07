package com.akilisha.espresso.jett.lifecycle;

import com.akilisha.espresso.jett.view.ViewEnginePlugins;
import com.akilisha.espresso.jett.view.ViewEnginesCache;
import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.api.plugin.IPluginCallback;
import com.akilisha.espresso.api.plugin.IViewEnginePlugin;
import com.akilisha.espresso.api.view.IViewEngine;

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
