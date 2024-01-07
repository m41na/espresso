package com.akilisha.espresso.jett.config;

import java.util.HashMap;

public class ConfigMap extends HashMap<String, Object> {

    public static final String MULTIPART_CONFIG = "multipart";

    public <T> T get(String key, Class<T> type) {
        return type.cast(get(key));
    }
}

