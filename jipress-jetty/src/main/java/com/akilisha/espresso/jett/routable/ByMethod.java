package com.akilisha.espresso.jett.routable;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.request.ReqMethod;
import com.akilisha.espresso.api.routeable.IMatched;
import com.akilisha.espresso.api.routeable.IRoutable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class ByMethod implements IRoutable {

    Map<ReqMethod, IRoutable> methodRoutes = new EnumMap<>(ReqMethod.class);

    @Override
    public void store(ReqMethod method, String path, IMiddleware[] handlers) {
        if (!methodRoutes.containsKey(method)) {
            methodRoutes.put(method, new ByPathRegex());
        }
        methodRoutes.get(method).store(method, path, handlers);
    }

    @Override
    public IMatched select(ReqMethod method, String path) {
        if (methodRoutes.containsKey(method)) {
            return methodRoutes.get(method).select(method, path);
        } else {
            //return matched info without any handlers
            return new MatchedInfo();
        }
    }

    @Override
    public void store(String path, IMiddleware... handlers) {
        Arrays.stream(ReqMethod.values()).forEach(method -> {
            if (!methodRoutes.containsKey(method)) {
                methodRoutes.put(method, new ByPathRegex());
            }
        });
        methodRoutes.values().forEach(route -> route.store(path, handlers));
    }

    @Override
    public boolean canRoute() {
        return !this.methodRoutes.isEmpty();
    }
}
