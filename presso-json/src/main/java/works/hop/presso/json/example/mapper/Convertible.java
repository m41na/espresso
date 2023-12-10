package works.hop.presso.json.example.mapper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

public interface Convertible {

    <T> void convert(T target, Properties properties);

    default <T> void convert(T target) {
        convert(target, new Properties());
    }

    default <T> void resolve(String propertyName, Object propertyValue, T target, Class<?> targetPropertyType, Properties properties){
        try {
            try {
                MethodHandle setter = setter(propertyName, targetPropertyType, target.getClass());
                setter.invoke(target, propertyValue);
            } catch (Throwable e) {
                String targetPropertyName = properties.getProperty(propertyName);
                try {
                    MethodHandle setter = setter(targetPropertyName, targetPropertyType, target.getClass());
                    setter.invoke(target, propertyValue);
                } catch (Throwable ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    default <T> void resolve(String propertyName, T target, Class<?> targetPropertyType, Properties properties){
        try {
            Class<?> srcPropertyType = getPropertyType(propertyName, properties);
            MethodHandle getter = getter(propertyName, srcPropertyType, getClass());
            Object srcPropertyValue = getter.invoke(this);
            resolve(propertyName, srcPropertyValue, target, targetPropertyType, properties);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> getPropertyType(String propertyName, Properties properties) {
        try {
            Field field = getClass().getDeclaredField(propertyName);  // private or protected access
            return field.getType();
        } catch (NoSuchFieldException | SecurityException e) {
            try {
                Field field = getClass().getField(propertyName);          // public access
                return field.getType();
            }
            catch (NoSuchFieldException | SecurityException e2) {
                return getPropertyType(properties.getProperty(propertyName), properties);
            }
        }
    }

    default MethodHandle setter(String property, Class<?> propertyType, Class<?> targetClass) throws IllegalAccessException, NoSuchMethodException {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodType setterType = MethodType.methodType(void.class, propertyType);
        String setterName = String.format("set%s%s", Character.toUpperCase(property.charAt(0)), property.substring(1));
        return lookup.findVirtual(targetClass, setterName, setterType);
    }

    default MethodHandle getter(String property, Class<?> propertyType, Class<?> targetClass) throws IllegalAccessException, NoSuchMethodException {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodType getterType = MethodType.methodType(propertyType);
        String getterName = String.format("get%s%s", Character.toUpperCase(property.charAt(0)), property.substring(1));
        return lookup.findVirtual(targetClass, getterName, getterType);
    }

    default void from(Map<String, Object> src, Properties properties){
        for(Map.Entry<String, Object> entry : src.entrySet()){
            Object srcPropertyValue = entry.getValue();
            resolve(entry.getKey(), src.get(entry.getKey()), this, srcPropertyValue.getClass(), properties);
        }
    }
}
