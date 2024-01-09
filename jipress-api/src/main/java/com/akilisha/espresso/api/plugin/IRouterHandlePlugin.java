package com.akilisha.espresso.api.plugin;

public interface IRouterHandlePlugin extends IRouterHandle, IPlugin<IRouterHandle> {

    @Override
    default IRouterHandle find(String identifier) {
        for (IRouterHandle plugin : loader()) {
            if (plugin.contextPath().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
