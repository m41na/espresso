package works.hop.presso.api.plugin;

import works.hop.presso.api.application.IApplication;

public interface IPluginLifecycle {

    void onInitialize(IApplication application);

    void onLoadPlugin(IPluginCallback callback);

    void onReloadPlugin();
}
