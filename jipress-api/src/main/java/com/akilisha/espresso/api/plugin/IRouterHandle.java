package com.akilisha.espresso.api.plugin;

import com.akilisha.espresso.api.router.IRouter;

public interface IRouterHandle {

    String contextPath();

    void init(IRouter app);
}
