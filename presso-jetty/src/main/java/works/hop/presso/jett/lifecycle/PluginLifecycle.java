package works.hop.presso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.plugin.IPluginCallback;
import works.hop.presso.api.plugin.IPluginLifecycle;

@Slf4j
public class PluginLifecycle implements IPluginLifecycle {

    IPluginCallback pluginCallback;
    IApplication application;

    @Override
    public void onInitApplication(IApplication application) {
        log.info("**onInitApplication**");
        this.application = application;
    }

    @Override
    public void onLoadPlugins(IPluginCallback callback) {
        log.info("**onLoadPlugins**");
        this.pluginCallback = callback;
        this.pluginCallback.onEvent(IPluginCallback.LOAD_PLUGINS, application);
    }

    @Override
    public void onReloadPlugins() {
        log.info("**onReloadPlugins**");
        this.pluginCallback.onEvent(IPluginCallback.RELOAD_PLUGINS, application);
    }
}
