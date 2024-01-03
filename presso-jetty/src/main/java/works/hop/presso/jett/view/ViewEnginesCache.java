package works.hop.presso.jett.view;

import works.hop.presso.api.view.IViewEngine;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewEnginesCache {

    private static final Map<String, IViewEngine> engines = new LinkedHashMap<>();

    private ViewEnginesCache() {
        //hide constructor
    }

    public static IViewEngine engine(String name, String templateDir) {
        if (engines.containsKey(name)) {
            IViewEngine viewEngine = engines.get(name);
            viewEngine.templateDir(templateDir);
            return viewEngine;
        }

        throw new NullPointerException(String.format("There is no template engine configured for the name %s", name));
    }

    public static void register(String name, IViewEngine viewEngine) {
        engines.putIfAbsent(name, viewEngine);
    }

    public static void deregister() {
        engines.clear();
    }
}
