package works.hop.presso.jett.view;

import works.hop.presso.api.plugin.IViewEnginePlugin;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.view.ViewEnginePlugin;

import java.util.ServiceLoader;

public class ViewEngineFactory {

    private static final IViewEnginePlugin plugins = new ViewEnginePlugin(ServiceLoader.load(IViewEngine.class));

    public static IViewEngine engine(String name, String templateDir) {
        IViewEngine engine = plugins.find(name);
        if (engine == null) {
            plugins.component().forEach(plugin -> {
                plugin.templateDir(templateDir);
            });
        }

        engine = plugins.find(name);
        if (engine == null) {
            throw new NullPointerException(String.format("There is no template engine configured for the name %s", name));
        }
        return engine;
    }
}
