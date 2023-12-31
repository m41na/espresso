package works.hop.presso.api.plugin;

import java.util.ServiceLoader;

public interface IPlugin<T> {

    void id(String value);

    String id();

    ServiceLoader<T> loader();

    void loader(ServiceLoader<T> loader);

    T find(String identifier);

    default void load(Class<T> type) {
        loader(ServiceLoader.load(type));
    }

    default void load(Class<T> type, ClassLoader parentCl) {
        loader(ServiceLoader.load(type, parentCl));
    }

    default void reload() {
        loader().reload();
    }
}
