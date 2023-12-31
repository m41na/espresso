package works.hop.presso.api.plugin;

import works.hop.presso.api.view.IViewEngine;

public interface IViewEnginePlugin extends IViewEngine, IPlugin<IViewEngine> {

    @Override
    default IViewEngine find(String identifier) {
        for (IViewEngine plugin : component()) {
            if (plugin.name().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
