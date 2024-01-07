package works.hop.presso.jett.plugin;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.plugin.*;
import works.hop.presso.api.router.IRouter;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.jett.content.BodyParserPlugins;
import works.hop.presso.jett.content.BodyParsersCache;
import works.hop.presso.jett.router.RouterHandlePlugins;
import works.hop.presso.jett.view.ViewEnginePlugins;
import works.hop.presso.jett.view.ViewEnginesCache;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;

import static works.hop.presso.api.plugin.Directories.pluginsClassLoader;

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
