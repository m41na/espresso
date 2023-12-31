package works.hop.presso.api.plugin;

import java.util.ServiceLoader;

public interface Plugin<T>  {

    void name(String name);

    String name();

    ServiceLoader<T> component();

    void component(ServiceLoader<T> loader);

    default void load(Class<T> type){
        component(ServiceLoader.load(type));
    }

    default void load(Class<T> type, ClassLoader parentCl){
        component(ServiceLoader.load(type, parentCl));
    }

    default void reload(){
        component().reload();
    }
}
