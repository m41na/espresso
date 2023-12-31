package works.hop.presso.api.plugin;

import works.hop.presso.api.application.IApplication;

public interface IPluginLifecycle {

    void onInitApplication(IApplication application);

    void onLoadPlugins(IPluginCallback<?> callback);

    void onReloadPlugins();
}
