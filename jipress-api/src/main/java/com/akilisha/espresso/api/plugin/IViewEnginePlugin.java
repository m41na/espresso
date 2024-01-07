package com.akilisha.espresso.api.plugin;

import com.akilisha.espresso.api.view.IViewEngine;

public interface IViewEnginePlugin extends IViewEngine, IPlugin<IViewEngine> {

    @Override
    default IViewEngine find(String identifier) {
        for (IViewEngine plugin : loader()) {
            if (plugin.name().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
