package com.akilisha.espresso.api.application;

import java.util.EnumMap;

public class CorsOptions extends EnumMap<CorsOptions.Option, String> {

    public CorsOptions() {
        super(Option.class);
    }

    public enum Option {
        // Request headers
        ACCESS_CONTROL_REQUEST_METHOD_HEADER("Access-Control-Request-Method", ""),
        ACCESS_CONTROL_REQUEST_HEADERS_HEADER("Access-Control-Request-Headers", ""),
        // Response headers
        ACCESS_CONTROL_ALLOW_ORIGIN_HEADER("Access-Control-Allow-Origin", "http://localhost:3000"),
        ACCESS_CONTROL_ALLOW_METHODS_HEADER("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD"),
        ACCESS_CONTROL_ALLOW_HEADERS_HEADER("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,Authorization,Options,Cache-Control,X-Requested-With"),
        ACCESS_CONTROL_MAX_AGE_HEADER("Access-Control-Max-Age", "1209600"),
        ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER("Access-Control-Allow-Credentials", "true"),
        ACCESS_CONTROL_EXPOSE_HEADERS_HEADER("Access-Control-Expose-Headers", ""),
        TIMING_ALLOW_ORIGIN_HEADER("Timing-Allow-Origin", ""),
        // Implementation constants
        ALLOWED_ORIGINS_PARAM("allowedOrigins", ""),
        ALLOWED_TIMING_ORIGINS_PARAM("allowedTimingOrigins", ""),
        ALLOWED_METHODS_PARAM("allowedMethods", ""),
        ALLOWED_HEADERS_PARAM("allowedHeaders", ""),
        PREFLIGHT_MAX_AGE_PARAM("preflightMaxAge", ""),
        ALLOW_CREDENTIALS_PARAM("allowCredentials", ""),
        EXPOSED_HEADERS_PARAM("exposedHeaders", ""),
        CHAIN_PREFLIGHT_PARAM("chainPreflight", ""),
        OLD_CHAIN_PREFLIGHT_PARAM("forwardPreflight", "");

        public final String name;
        public final String defaultValue;

        Option(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public static Option option(String name) {
            for (Option opt : values()) {
                if (opt.name.equals(name)) {
                    return opt;
                }
            }
            throw new RuntimeException(String.format("Cors option '%s' is not a known option", name));
        }
    }
}
