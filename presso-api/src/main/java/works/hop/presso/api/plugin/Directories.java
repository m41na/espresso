package works.hop.presso.api.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumMap;
import java.util.Objects;

public class Directories extends EnumMap<DirectoryInfo, String> {

    public static final Directories PLUGINS = new Directories();

    private Directories() {
        super(DirectoryInfo.class);
    }

    public static URLClassLoader pluginsClassLoader(String pluginDir) {
        File loc = new File(pluginDir);
        if (loc.exists()) {
            File[] jars = loc.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
            try {
                URL[] urls = new URL[Objects.requireNonNull(jars).length];
                for (int i = 0; i < jars.length; i++)
                    urls[i] = jars[i].toURI().toURL();
                return new URLClassLoader(urls);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
