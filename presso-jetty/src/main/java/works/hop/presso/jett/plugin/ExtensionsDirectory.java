package works.hop.presso.jett.plugin;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import works.hop.presso.api.extension.IExtension;
import works.hop.presso.api.extension.IExtensionCallback;
import works.hop.presso.api.extension.IExtensionPlugin;
import works.hop.presso.api.plugin.Directories;
import works.hop.presso.api.plugin.DirectoryInfo;
import works.hop.presso.jett.extension.ExtensionPlugins;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;

import static works.hop.presso.api.plugin.Directories.pluginsClassLoader;

@Slf4j
public class ExtensionsDirectory implements IExtensionCallback<ContextHandlerCollection> {

    @Override
    public void onExtend(ContextHandlerCollection extensionPoint) {
        log.info("Start loading extensions");
        String pluginDir = Path.of(Directories.PLUGINS.get(DirectoryInfo.PLUGINS_HOME), Directories.PLUGINS.get(DirectoryInfo.CTX_EXTENSIONS)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IExtensionPlugin plugins = new ExtensionPlugins(ServiceLoader.load(IExtension.class, ucl));

            plugins.loader().forEach(ext -> ext.extendWith(extensionPoint));
        });
    }
}
