package works.hop.presso.api.plugin;

import works.hop.presso.api.router.IRouter;

public interface IRouterHandle {

    String contextPath();

    void init(IRouter app);
}
