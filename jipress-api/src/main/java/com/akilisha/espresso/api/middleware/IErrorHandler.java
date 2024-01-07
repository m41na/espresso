package com.akilisha.espresso.api.middleware;

import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

public interface IErrorHandler extends IMiddleware {

    void handle(Exception error, IRequest req, IResponse res, INext next);

    default Boolean isHandled() {
        return false;
    }

    default void handle(IRequest req, IResponse res, INext next) {
        this.handle(null, req, res, next);
    }
}
