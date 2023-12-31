package works.hop.presso.api.plugin;

import works.hop.presso.api.view.IViewEngine;

public interface IViewEnginePlugin extends IViewEngine, Plugin<IViewEngine> {

    default IViewEngine find(String name){
        for (IViewEngine plugin : component()) {
            if (plugin.name().equals(name)) {
                return plugin;
            }
        }
        return null;
    }
}
