package works.hop.presso.api.routeable;

import works.hop.presso.api.middleware.IMiddleware;

import java.util.Map;

public interface IMatched {

    IMiddleware[] getHandlers();

    void setHandlers(IMiddleware[] handlers);

    Map<String, String> getParams();

    void setParams(Map<String, String> params);
}
