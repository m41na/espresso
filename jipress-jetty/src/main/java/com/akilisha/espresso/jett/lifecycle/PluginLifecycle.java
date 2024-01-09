package com.akilisha.espresso.jett.lifecycle;

import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.api.plugin.IPluginCallback;
import com.akilisha.espresso.api.plugin.IPluginLifecycle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PluginLifecycle implements IPluginLifecycle {

    IPluginCallback pluginCallback;
    IApplication application;

    @Override
    public void onInitialize(IApplication application) {
        log.info("**onInitialize for {}**", PluginLifecycle.class.getName());
        this.application = application;
    }

    @Override
    public void onLoadPlugin(IPluginCallback callback) {
        log.info("**onLoadPlugins for {}**", PluginLifecycle.class.getName());
        this.pluginCallback = callback;
        this.pluginCallback.onEvent(IPluginCallback.LOAD_PLUGINS, application);
    }

    @Override
    public void onReloadPlugin() {
        log.info("**onReloadPlugins for {}**", PluginLifecycle.class.getName());
        this.pluginCallback.onEvent(IPluginCallback.RELOAD_PLUGINS, application);
    }
}
