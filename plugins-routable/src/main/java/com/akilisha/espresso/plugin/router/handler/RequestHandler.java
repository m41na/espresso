package com.akilisha.espresso.plugin.router.handler;

import lombok.RequiredArgsConstructor;
import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.middleware.INext;
import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

@RequiredArgsConstructor
public class RequestHandler implements IMiddleware {

    final String message;

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        res.send(this.message);
    }
}
