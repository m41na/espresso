package com.akilisha.espresso.jett.handler;

import com.akilisha.espresso.api.application.CorsOptions;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.annotation.ManagedObject;

import java.io.IOException;

@ManagedObject("Apply requested CORS headers in the response")
@RequiredArgsConstructor
@Slf4j
public class CorsHandler extends HandlerWrapper {

    final CorsOptions options;

    private static boolean isPreflightRequest(HttpServletRequest request) {
        return request.getHeader("Origin") != null && request.getMethod().equalsIgnoreCase("OPTIONS");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("Origin header - {}", request.getHeader("Origin"));
        log.info("Request method - {}", request.getMethod());

        response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER.name, CorsOptions.Option.ACCESS_CONTROL_ALLOW_HEADERS_HEADER.defaultValue);
        response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER.name, CorsOptions.Option.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER.defaultValue);

        if (isPreflightRequest(request)) {
            //TODO: This is VERY hastily put together. Needs a lot more reasoning about scenarios. This is a temporary solution for now
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER.name, CorsOptions.Option.ACCESS_CONTROL_MAX_AGE_HEADER.defaultValue);
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHODS_HEADER.name, CorsOptions.Option.ACCESS_CONTROL_ALLOW_METHODS_HEADER.defaultValue);
            response.addHeader(CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER.name, CorsOptions.Option.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER.defaultValue);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            baseRequest.setHandled(true);
            return;
        }

        if (_handler != null)
            _handler.handle(target, baseRequest, request, response);
    }
}

