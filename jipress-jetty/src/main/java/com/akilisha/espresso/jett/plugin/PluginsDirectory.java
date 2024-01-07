package com.akilisha.espresso.jett.plugin;

import com.akilisha.espresso.api.plugin.*;
import com.akilisha.espresso.jett.content.BodyParserPlugins;
import com.akilisha.espresso.jett.content.BodyParsersCache;
import com.akilisha.espresso.jett.router.RouterHandlePlugins;
import com.akilisha.espresso.jett.view.ViewEnginePlugins;
import com.akilisha.espresso.jett.view.ViewEnginesCache;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.api.content.IBodyParser;
import com.akilisha.espresso.api.router.IRouter;
import com.akilisha.espresso.api.view.IViewEngine;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;

import static com.akilisha.espresso.api.plugin.Directories.pluginsClassLoader;

@Setter
@Slf4j
public class PluginsDirectory implements IPluginCallback {

    @Override
    public void reloadPlugins(IApplication app) {
        log.warn("The 'reloadPlugins' action will require a restart since it involves reloading router handle plugins");
    }

    @Override
    public void loadPlugins(IApplication app) {
        for (DirectoryInfo layout : Directories.PLUGINS.keySet()) {
            switch (layout) {
                case ROUTER_HANDLES -> {
                    loadRouterHandlePlugins(app);
                }
                case BODY_PARSERS -> {
                    loadBodyParserPlugins();
                }
                case VIEW_ENGINES -> {
                    loadViewEnginePlugins();
                }
            }
        }
    }

    private void loadViewEnginePlugins() {
        String pluginDir = Path.of(Directories.PLUGINS.get(DirectoryInfo.PLUGINS_HOME), Directories.PLUGINS.get(DirectoryInfo.VIEW_ENGINES)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IViewEnginePlugin viewPlugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class, ucl));

            viewPlugins.loader().forEach(engine -> ViewEnginesCache.register(engine.name(), engine));
        });
    }

    private void loadBodyParserPlugins() {
        String pluginDir = Path.of(Directories.PLUGINS.get(DirectoryInfo.PLUGINS_HOME), Directories.PLUGINS.get(DirectoryInfo.BODY_PARSERS)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IBodyParserPlugin contentPlugins = new BodyParserPlugins(ServiceLoader.load(IBodyParser.class, ucl));

            contentPlugins.loader().forEach(parser -> BodyParsersCache.register(parser.contentType(), parser));
        });
    }

    private void loadRouterHandlePlugins(IApplication app) {
        String pluginDir = Path.of(Directories.PLUGINS.get(DirectoryInfo.PLUGINS_HOME), Directories.PLUGINS.get(DirectoryInfo.ROUTER_HANDLES)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IRouterHandlePlugin routerPlugins = new RouterHandlePlugins(ServiceLoader.load(IRouterHandle.class, ucl));

            routerPlugins.loader().forEach(plugin -> {
                IRouter router = app.route(plugin.contextPath());
                plugin.init(router);
            });
        });
    }


}
