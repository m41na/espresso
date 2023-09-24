package works.hop.presso.api.application;

import java.util.EnumMap;

public class AppSettings extends EnumMap<AppSettings.Setting, Object> {

    public AppSettings() {
        super(Setting.class);
        //apply default values
        put(Setting.ACCEPTOR_THREADS, 1);
        put(Setting.SELECTOR_THREADS, 1);
        put(Setting.ACCEPT_QUEUE_SIZE, 128);
    }

    public enum Setting {
        CASE_SENSITIVE_ROUTING("case sensitive routing"),
        ENV("env"),
        ETAG("etag"),
        JSONP_CALLBACK("jsonp callback"),
        JSON_ESCAPE("json escape"),
        JSON_REPLACER("json replacer"),
        JSON_SPACES("json spaces"),
        QUERY_PARSER("query parser"),
        VIEW_ENGINE("view engine"),
        TEMPLATES_DIR("templates dir"),
        TEMPLATES_EXT("templates ext"),
        ACCEPTOR_THREADS("acceptors"),
        SELECTOR_THREADS("selectors"),
        ACCEPT_QUEUE_SIZE("acceptQueueSize"),
        TRUST_PROXY("trust proxy");

        public final String property;

        Setting(String value) {
            this.property = value;
        }

        public static Setting value(String name) {
            for (Setting setting : values()) {
                if (setting.property.equals(name)) {
                    return setting;
                }
            }
            throw new RuntimeException("Unknown application setting");
        }
    }
}
