package works.hop.presso.jett.routable;

import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.request.ReqMethod;
import works.hop.presso.api.routeable.IMatched;
import works.hop.presso.api.routeable.IRoutable;
import works.hop.presso.jett.application.PathUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class ByPathRegex implements IRoutable {

    Map<String, IMiddleware[]> middlewares = new LinkedHashMap<>();
    Map<String, IMiddleware[]> all = new LinkedHashMap<>();

    @Override
    public void store(ReqMethod method, String path, IMiddleware[] handlers) {
        boolean contains = middlewares.keySet().stream()
                .anyMatch(key -> key.equals(path));
        if (!contains) {
            middlewares.put(path, handlers);
        } else {
            throw new RuntimeException("This pattern is already matched");
        }
    }

    @Override
    public IMatched select(ReqMethod method, String path) {
        IMatched matchedInfo = new MatchedInfo();

        for (Map.Entry<String, IMiddleware[]> entry : middlewares.entrySet()) {
            String pathRegex = PathUtils.pathToRegex(entry.getKey());
            if (Pattern.matches(pathRegex, path)) {
                matchedInfo.setParams(PathUtils.extractPathVariables(entry.getKey(), path));
                matchedInfo.setHandlers(entry.getValue());
                break;
            }
        }

        if (matchedInfo.getHandlers() != null) {
            IMiddleware[] handlers = matchedInfo.getHandlers();
            Optional<IMiddleware[]> before = all.entrySet().stream()
                    .filter(entry -> Pattern.matches(entry.getKey(), path))
                    .map(Map.Entry::getValue)
                    .findFirst();

            if (before.isPresent()) {
                IMiddleware[] merged = new IMiddleware[handlers.length + before.get().length];
                System.arraycopy(before.get(), 0, merged, 0, before.get().length); //put "before" first
                System.arraycopy(handlers, 0, merged, before.get().length, handlers.length);
                matchedInfo.setHandlers(merged);
            }
        }
        return matchedInfo;
    }

    @Override
    public void store(String path, IMiddleware... handlers) {
        String key = path.replace("*", ".*");
        if (all.get(key) != null) {
            IMiddleware[] newArray = new IMiddleware[all.get(key).length + handlers.length];
            System.arraycopy(all.get(key), 0, newArray, 0, all.get(key).length);
            System.arraycopy(handlers, 0, newArray, all.get(key).length, handlers.length);
            all.put(key, newArray);
        } else {
            all.put(key, handlers);
        }
    }

    @Override
    public boolean canRoute() {
        return !this.middlewares.isEmpty();
    }
}
