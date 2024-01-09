package com.akilisha.espresso.api.middleware;

import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

public interface IParamCallback extends IMiddleware {

    void handle(IRequest req, IResponse res, INext next, Object paramValue);

    @Override
    default void handle(IRequest req, IResponse res, INext next) {
        this.handle(req, res, next, null);
    }
}
