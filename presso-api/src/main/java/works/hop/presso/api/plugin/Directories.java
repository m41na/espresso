package works.hop.presso.api.plugin;

import java.util.EnumMap;

public class Directories extends EnumMap<DirectoryInfo, String> {

    public static final Directories PLUGINS = new Directories();

    private Directories() {
        super(DirectoryInfo.class);
    }
}
