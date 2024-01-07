package com.akilisha.espresso.api.router;

import com.akilisha.espresso.api.middleware.IMiddleware;

public interface IRouteOptions {

    boolean caseSensitive();

    boolean mergeParams();

    boolean appMounted();

    String originalPath();

    void onMounted(String context, Integer length, String pattern, IMiddleware handler);
}
