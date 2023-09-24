package works.hop.presso.jett.routable;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.request.ReqMethod;
import works.hop.presso.api.routeable.IMatched;
import works.hop.presso.api.routeable.IRoutable;

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
        return methodRoutes.get(method).select(method, path);
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
