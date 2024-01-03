package works.hop.presso.jett.plugin;

import lombok.Setter;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.plugin.*;
import works.hop.presso.api.router.IRouter;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.jett.content.BodyParserFactory;
import works.hop.presso.jett.content.BodyParserPlugins;
import works.hop.presso.jett.router.RouterHandlePlugins;
import works.hop.presso.jett.view.ViewEngineFactory;
import works.hop.presso.jett.view.ViewEnginePlugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

@Setter
public class PluginsDirectory implements IPluginCallback {

    @Override
    public void reloadPlugins(IApplication app) {
        loadPlugins(app);
        //TODO: figure out how to refresh static cache inside Factory class
    }

    @Override
    public void loadPlugins(IApplication app) {
        for (PluginsDir layout : DirLayout.PLUGINS.keySet()) {
            switch (layout) {
                case ROUTER_HANDLE_PLUGINS -> {
                    loadRouterHandlePlugins(app);
                }
                case BODY_PARSER_PLUGINS -> {
                    loadBodyParserPlugins();
                }
                case VIEW_ENGINE_PLUGINS -> {
                    loadViewEnginePlugins();
                }
            }
        }
    }

    private void loadViewEnginePlugins() {
        String pluginDir = Path.of(DirLayout.PLUGINS.get(PluginsDir.PLUGINS_HOME_DIR), DirLayout.PLUGINS.get(PluginsDir.VIEW_ENGINE_PLUGINS)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IViewEnginePlugin viewPlugins = new ViewEnginePlugins(ServiceLoader.load(IViewEngine.class, ucl));

            viewPlugins.loader().forEach(engine -> ViewEngineFactory.register(engine.name(), engine));
        });
    }

    private void loadBodyParserPlugins() {
        String pluginDir = Path.of(DirLayout.PLUGINS.get(PluginsDir.PLUGINS_HOME_DIR), DirLayout.PLUGINS.get(PluginsDir.BODY_PARSER_PLUGINS)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IBodyParserPlugin contentPlugins = new BodyParserPlugins(ServiceLoader.load(IBodyParser.class, ucl));

            contentPlugins.loader().forEach(parser -> BodyParserFactory.register(parser.contentType(), parser));
        });
    }

    private void loadRouterHandlePlugins(IApplication app) {
        String pluginDir = Path.of(DirLayout.PLUGINS.get(PluginsDir.PLUGINS_HOME_DIR), DirLayout.PLUGINS.get(PluginsDir.ROUTER_HANDLE_PLUGINS)).toString();
        Optional.ofNullable(pluginsClassLoader(pluginDir)).ifPresent((URLClassLoader ucl) -> {
            IRouterHandlePlugin routerPlugins = new RouterHandlePlugins(ServiceLoader.load(IRouterHandle.class, ucl));

            routerPlugins.loader().forEach(plugin -> {
                IRouter router = app.route(plugin.contextPath());
                plugin.init(router);
            });
        });
    }

    private URLClassLoader pluginsClassLoader(String pluginDir) {
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
