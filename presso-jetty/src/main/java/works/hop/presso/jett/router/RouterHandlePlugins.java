package works.hop.presso.jett.router;

import works.hop.presso.api.plugin.IRouterHandle;
import works.hop.presso.api.plugin.IRouterHandlePlugin;
import works.hop.presso.api.router.IRouter;

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
