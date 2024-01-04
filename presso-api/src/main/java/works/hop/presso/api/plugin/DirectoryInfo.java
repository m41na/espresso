package works.hop.presso.api.plugin;

public enum DirectoryInfo {

    PLUGINS_HOME("pluginsHome", "plugins home directory", System.getProperty("user.dir")),
    VIEW_ENGINES("viewEngines", "view engine plugins sub-directory", "/view"),
    BODY_PARSERS("bodyParsers", "body parser plugins sub-directory", "/content"),
    ROUTER_HANDLES("routerHandles", "router handler plugins sub-directory", "/router");

    public final String folder;
    public final String description;
    public final String path;

    DirectoryInfo(String folder, String description, String path) {
        this.folder = folder;
        this.description = description;
        this.path = path;
    }

    public static DirectoryInfo name(String name) {
        for (DirectoryInfo info : values()) {
            if (info.folder.equals(name)) {
                return info;
            }
        }
        throw new RuntimeException("Unknown directory/folder");
    }
}
