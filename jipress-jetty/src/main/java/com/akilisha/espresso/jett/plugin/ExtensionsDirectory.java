package com.akilisha.espresso.jett.plugin;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import com.akilisha.espresso.api.extension.IExtension;
import com.akilisha.espresso.api.extension.IExtensionCallback;
import com.akilisha.espresso.api.extension.IExtensionPlugin;
import com.akilisha.espresso.api.plugin.Directories;
import com.akilisha.espresso.api.plugin.DirectoryInfo;
import com.akilisha.espresso.jett.extension.ExtensionPlugins;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;

import static com.akilisha.espresso.api.plugin.Directories.pluginsClassLoader;

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
