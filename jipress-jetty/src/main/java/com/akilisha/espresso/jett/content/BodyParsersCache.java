package com.akilisha.espresso.jett.content;

import com.akilisha.espresso.api.content.IBodyParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class BodyParsersCache {

    private static final Map<String, IBodyParser> parsers = new LinkedHashMap<>();

    private BodyParsersCache() {
        //hide constructor
    }

    public static IBodyParser parser(String contentType) {
        if (parsers.containsKey(contentType)) {
            return parsers.get(contentType);
        }
        throw new NullPointerException(String.format("There is no content parser configured for the type %s", contentType));
    }

    public static void register(String contentType, IBodyParser parser) {
        parsers.putIfAbsent(contentType, parser);
    }

    public static void deregister() {
        parsers.clear();
    }
}
