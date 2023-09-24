package works.hop.presso.jett.application;

import works.hop.presso.api.middleware.IErrorHandler;
import works.hop.presso.api.middleware.INext;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

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
