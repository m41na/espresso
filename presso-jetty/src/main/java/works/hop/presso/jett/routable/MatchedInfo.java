package works.hop.presso.jett.routable;

import lombok.Data;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.routeable.IMatched;

import java.util.Collections;
import java.util.Map;

@Data
public class MatchedInfo implements IMatched {

    IMiddleware[] handlers;
    Map<String, String> params = Collections.emptyMap();
}
