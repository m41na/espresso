package com.akilisha.espresso.plugin.router.handler;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.middleware.INext;
import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestHandler implements IMiddleware {

    final String message;

    @Override
    public void handle(IRequest req, IResponse res, INext next) {
        res.send(this.message);
    }
}
