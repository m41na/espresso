package com.akilisha.espresso.api.routeable;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.request.ReqMethod;

public interface IRoutable {

    void store(ReqMethod method, String path, IMiddleware... handlers);

    IMatched select(ReqMethod method, String path);

    void store(String path, IMiddleware... handlers);

    boolean canRoute();
}
