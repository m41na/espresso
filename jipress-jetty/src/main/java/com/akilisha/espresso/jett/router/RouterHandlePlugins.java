package com.akilisha.espresso.jett.router;

import com.akilisha.espresso.api.plugin.IRouterHandle;
import com.akilisha.espresso.api.plugin.IRouterHandlePlugin;
import com.akilisha.espresso.api.router.IRouter;

import java.util.ServiceLoader;

public class RouterHandlePlugins implements IRouterHandlePlugin {

    private ServiceLoader<IRouterHandle> loader;
    private String contextPath;

    public RouterHandlePlugins(ServiceLoader<IRouterHandle> loader) {
        this.loader = loader;
    }

    @Override
    public void id(String value) {
        this.contextPath = value;
    }

    @Override
    public String id() {
        return this.contextPath;
    }

    @Override
    public ServiceLoader<IRouterHandle> loader() {
        return this.loader;
    }

    @Override
    public void loader(ServiceLoader<IRouterHandle> loader) {
        this.loader = loader;
    }

    @Override
    public String contextPath() {
        return this.id();
    }

    @Override
    public void init(IRouter app) {
        find(contextPath).init(app);
    }
}
