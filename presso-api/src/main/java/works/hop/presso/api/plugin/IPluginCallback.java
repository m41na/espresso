package works.hop.presso.api.plugin;

public interface IPluginCallback<T> {

    String LOAD_PLUGINS = "LOAD_PLUGINS";
    String RELOAD_PLUGINS = "RELOAD_PLUGINS";

    default void onEvent(String event, T factory) {
        switch (event) {
            case LOAD_PLUGINS:
                loadPlugins(factory);
                break;
            case RELOAD_PLUGINS:
                reloadPlugins(factory);
                break;
            default:
                throw new RuntimeException(String.format("%s event is unknown", event));
        }
    }

    void reloadPlugins(T factory);

    void loadPlugins(T factory);
}
