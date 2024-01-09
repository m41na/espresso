package com.akilisha.espresso.api.middleware;

import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

public interface IMiddleware {

    default void handle(IRequest req, IResponse res) {
        handle(req, res, null);
    }

    void handle(IRequest req, IResponse res, INext next);
}
