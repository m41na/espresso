package com.akilisha.espresso.jett.application;

import com.akilisha.espresso.api.middleware.IErrorHandler;
import com.akilisha.espresso.api.middleware.INext;
import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;

import java.nio.charset.Charset;

public class DefaultErrHandler implements IErrorHandler {

    @Override
    public void handle(Exception error, IRequest req, IResponse res, INext next) {
        if (next.errorCode() == null) {
            String message = String.format("""
                    You are using the default exception handler. Consider configuring
                    more specific exception handlers having error codes to manage your
                    exceptions more gracefully.
                    %s
                    """, error.getMessage());
            res.end(message, Charset.defaultCharset().name());
            next.setHandled(true);
        }
    }
}
