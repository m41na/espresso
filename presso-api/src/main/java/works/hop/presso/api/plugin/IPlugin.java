package works.hop.presso.api.plugin;

import java.util.ServiceLoader;

public interface IPlugin<T> {

    void identifier(String value);

    String identifier();

    ServiceLoader<T> component();

    void component(ServiceLoader<T> loader);

    T find(String identifier);

    default void load(Class<T> type) {
        component(ServiceLoader.load(type));
    }

    default void load(Class<T> type, ClassLoader parentCl) {
        component(ServiceLoader.load(type, parentCl));
    }

    default void reload() {
        component().reload();
    }
}
