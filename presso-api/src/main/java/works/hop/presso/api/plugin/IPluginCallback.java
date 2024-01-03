package works.hop.presso.api.plugin;

import works.hop.presso.api.application.IApplication;

public interface IPluginCallback {

    String LOAD_PLUGINS = "LOAD_PLUGINS";
    String RELOAD_PLUGINS = "RELOAD_PLUGINS";

    default void onEvent(String event, IApplication app) {
        switch (event) {
            case LOAD_PLUGINS:
                loadPlugins(app);
                break;
            case RELOAD_PLUGINS:
                reloadPlugins(app);
                break;
            default:
                throw new RuntimeException(String.format("%s event is unknown", event));
        }
    }

    void reloadPlugins(IApplication app);

    void loadPlugins(IApplication app);
}
