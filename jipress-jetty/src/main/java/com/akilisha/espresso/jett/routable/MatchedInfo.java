package com.akilisha.espresso.jett.routable;

import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.routeable.IMatched;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class MatchedInfo implements IMatched {

    IMiddleware[] handlers;
    Map<String, String> params = Collections.emptyMap();
}
