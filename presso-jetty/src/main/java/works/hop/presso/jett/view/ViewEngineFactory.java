package works.hop.presso.jett.view;

import works.hop.presso.api.view.IViewEngine;

import java.util.HashMap;
import java.util.Map;

public class ViewEngineFactory {

    private static final Map<String, IViewEngine> factories = new HashMap<>();

    public static IViewEngine engine(String name, String templateDir) {
        if (factories.containsKey(name)) {
            return factories.get(name);
        }

        if (IViewEngine.MVEL.equals(name)) {
            IViewEngine viewEngine = factories.getOrDefault(name, new MvelViewEngine(templateDir));
            factories.putIfAbsent(name, viewEngine);
            return viewEngine;
        }

        throw new NullPointerException(String.format("There is no template engine configured for the name %s", name));
    }

    public static void register(String name, IViewEngine viewEngine) {
        factories.putIfAbsent(name, viewEngine);
    }
}
