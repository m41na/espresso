package works.hop.presso.api.application;

public interface ILifeCycle {

    void onInitApplication();

    void beforeLoadPlugins();

    void onLoadPlugins();

    void beforeReloadPlugins();

    void onReloadPlugins();
}
