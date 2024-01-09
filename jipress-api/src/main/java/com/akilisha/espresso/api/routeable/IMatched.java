package com.akilisha.espresso.api.routeable;

import com.akilisha.espresso.api.middleware.IMiddleware;

import java.util.Map;

public interface IMatched {

    IMiddleware[] getHandlers();

    void setHandlers(IMiddleware[] handlers);

    Map<String, String> getParams();

    void setParams(Map<String, String> params);
}
