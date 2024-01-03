package works.hop.presso.api.plugin;

public enum PluginsDir {

    PLUGINS_HOME_DIR("pluginsHome", "plugins home directory", System.getProperty("user.dir")),
    VIEW_ENGINE_PLUGINS("viewPlugins", "view engine plugins sub-directory", "/view"),
    BODY_PARSER_PLUGINS("contentPlugins", "body parser plugins sub-directory", "/content"),
    ROUTER_HANDLE_PLUGINS("routerPlugins", "router handler plugins sub-directory", "/router");

    public final String folder;
    public final String description;
    public final String path;

    PluginsDir(String folder, String description, String path) {
        this.folder = folder;
        this.description = description;
        this.path = path;
    }

    public static PluginsDir name(String name) {
        for (PluginsDir setting : values()) {
            if (setting.folder.equals(name)) {
                return setting;
            }
        }
        throw new RuntimeException("Unknown startup property");
    }
}
