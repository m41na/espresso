package works.hop.presso.jett.plugin;

import works.hop.presso.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;

public class PluginsDirectory {

    private final static Set<Plugin<?>> pluginLoaders = new HashSet<>();

    private PluginsDirectory(){
        //hide constructor
    }

    public static void load(String pluginDir) throws IOException {
        File loc = new File(pluginDir);

        File[] jars = loc.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
        URL[] urls = new URL[Objects.requireNonNull(jars).length];
        for (int i = 0; i < jars.length; i++)
            urls[i] = jars[i].toURI().toURL();
        URLClassLoader ucl = new URLClassLoader(urls);

        ServiceLoader<Plugin> service = ServiceLoader.load(Plugin.class, ucl);
//        new PluginLoader(service, pluginLoaders::add) {
//            @Override
//            public String name() {
//                return PluginsDirectory.class.getName();
//            }
//        };
    }

    public static void list(){
        for (Plugin<?> loader : pluginLoaders){
//            loader.list();
        }
    }
}
