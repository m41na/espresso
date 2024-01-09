package com.akilisha.espresso.plugin.router.handler;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.middleware.INext;
import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

public class BeforeAllHandler implements IMiddleware {
    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        res.send("ALL Holla Mundo!");
    }
}
