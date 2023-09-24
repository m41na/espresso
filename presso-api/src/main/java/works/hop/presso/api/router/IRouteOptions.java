package works.hop.presso.api.router;

import works.hop.presso.api.middleware.IMiddleware;

public interface IRouteOptions {

    boolean caseSensitive();

    boolean mergeParams();

    boolean appMounted();

    String originalPath();

    void onMounted(String context, Integer length, String pattern, IMiddleware handler);
}
