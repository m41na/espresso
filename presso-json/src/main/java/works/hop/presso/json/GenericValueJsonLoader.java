package works.hop.presso.json;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.invoke.MethodType.methodType;

@RequiredArgsConstructor
public class GenericValueJsonLoader<T> implements JsonLoader<T> {

    final Class<?> type;
    final String configFile;
    Stack<NodeValue<?>> parentStack = new Stack<>();
    Stack<String> methodStack = new Stack<>();

    public static void parseJsonPayload(JsonParser parser, Class<?> parentType, Stack<NodeValue<?>> parentStack, Stack<String> methodStack, Properties configuration, Consumer<NodeValue<?>> consumer) {
        if (parser.hasNext()) {
            final JsonParser.Event event = parser.next();
            switch (event) {
                case KEY_NAME:
                    String keyValue = parser.getString();
                    System.out.println(keyValue);
                    methodStack.push(keyValue);
                    parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    break;
                case VALUE_STRING: {
                    NodeValue<String> value = new NodeValue<>(parser.getString());
                    System.out.println(value.getValue(String.class));

                    Field field = getFieldInfo(parentType, methodStack.peek());
                    if (field.getType().equals(String.class)) {
                        // check that value is a String before applying it to the parent object
                        invokeSetterHandle(parentType, parentStack, methodStack, parser.getString(), String.class);
                        parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    } else {
                        // consider that the value could be an enum
                        Optional<?> optionalEnum = Arrays.stream(field.getType().getEnumConstants())
                                .filter(e -> e.toString().equals(value.getValue())).findFirst();
                        if (optionalEnum.isPresent()) {
                            invokeSetterHandle(parentType, parentStack, methodStack, optionalEnum.get(), field.getType());
                            parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                        } else {
                            //TODO: what else could the value type be?
                            throw new RuntimeException("Unexpected type - " + field.getType());
                        }
                    }
                    break;
                }
                case VALUE_NUMBER: {
                    NodeValue<BigDecimal> value = new NodeValue<>(parser.getBigDecimal());
                    System.out.println(value.getValue(BigDecimal.class));

                    Field field = getFieldInfo(parentType, methodStack.peek());
                    Class<?> fieldType = Objects.requireNonNull(field, "field value should not be null").getType();
                    Object resolvedValue = resolveNumericValue(parser.getBigDecimal(), fieldType);

                    invokeSetterHandle(parentType, parentStack, methodStack, resolvedValue, fieldType);
                    parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);

                    break;
                }
                case VALUE_TRUE: {
                    NodeValue<Boolean> value = new NodeValue<>(true);
                    System.out.println(value.getValue(Boolean.class));

                    invokeSetterHandle(parentType, parentStack, methodStack, true, Boolean.class);
                    parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    break;
                }
                case VALUE_FALSE: {
                    NodeValue<Boolean> value = new NodeValue<>(false);
                    System.out.println(value.getValue());

                    invokeSetterHandle(parentType, parentStack, methodStack, false, Boolean.class);
                    parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    break;
                }
                case START_OBJECT: {
                    System.out.println(event);
                    if (methodStack.isEmpty()) {
                        Object instance = invokeNoArgsConstructor(parentType);
                        parentStack.push(new NodeValue<>(instance));
                        parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    } else {
                        //if the parent is a map type, figure out the type contained (from configuration)
                        if (Map.class.isAssignableFrom(parentType)) {
                            //Temporary pop to reach propertyName
                            String temp = methodStack.pop();
                            Optional<String> optionalGenericType = identityMapComponentType(!methodStack.isEmpty() ? methodStack.peek() : "", configuration);
                            methodStack.push(temp);
                            String genericType = optionalGenericType.orElseThrow(() -> new RuntimeException("Unidentified component type for Map entity"));
                            //generic type identified
                            try {
                                Class<?> type = Class.forName(genericType);
                                Object instance = invokeNoArgsConstructor(type);
                                parentStack.push(new NodeValue<>(instance));
                                parseJsonPayload(parser, type, parentStack, methodStack, configuration, consumer);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //if the parent is a collection type, figure out the type contained (from configuration)
                        else if (Collection.class.isAssignableFrom(parentType) || Collection.class.isAssignableFrom(parentStack.peek().getValue().getClass())) {
                            Optional<String> optionalGenericType = identityListComponentType(methodStack.peek(), configuration);
                            String genericType = optionalGenericType.orElseThrow(() -> new RuntimeException("Unidentified component type for Collection entity"));
                            //generic type identified
                            try {
                                Class<?> type = Class.forName(genericType);
                                Object instance = invokeNoArgsConstructor(type);
                                parentStack.push(new NodeValue<>(instance));
                                parseJsonPayload(parser, type, parentStack, methodStack, configuration, consumer);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (parentType.isArray()) {
                            Class<?> type = parentType.componentType();
                            Object instance = invokeNoArgsConstructor(type);
                            parentStack.push(new NodeValue<>(instance));
                            parseJsonPayload(parser, type, parentStack, methodStack, configuration, consumer);
                        } else {
                            Optional<String> optionalGenericType = identifyConfiguredType(methodStack.peek(), configuration);
                            if (optionalGenericType.isPresent()) {
                                try {
                                    Class<?> type = Class.forName(optionalGenericType.get());
                                    Object instance = invokeNoArgsConstructor(type);
                                    parentStack.push(new NodeValue<>(instance));
                                    parseJsonPayload(parser, type, parentStack, methodStack, configuration, consumer);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Field field = getFieldInfo(parentType, methodStack.peek());
                                Object instance = invokeNoArgsConstructor(field.getType());
                                parentStack.add(new NodeValue<>(instance));
                                parseJsonPayload(parser, field.getType(), parentStack, methodStack, configuration, consumer);
                            }
                        }
                    }
                    break;
                }
                case START_ARRAY: {
                    System.out.println(event);
                    if (methodStack.isEmpty()) {
                        Optional<String> optionalGenericType = identityListComponentType("", configuration);
                        String genericType = optionalGenericType.orElseThrow(() -> new RuntimeException("Unidentified generic type for collection entity"));
                        //generic type identified
                        try {
                            Object instance = invokeCollectionConstructor(parentType, configuration, "");
                            parentStack.push(new NodeValue<>(instance));

                            Class<?> type = Class.forName(genericType);
                            parseJsonPayload(parser, type, parentStack, methodStack, configuration, consumer);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        Field field = getFieldInfo(parentType, methodStack.peek());
                        Object instance = invokeCollectionConstructor(field.getType(), configuration, methodStack.peek());
                        parentStack.push(new NodeValue<>(instance));
                        parseJsonPayload(parser, field.getType(), parentStack, methodStack, configuration, consumer);
                    }
                    break;
                }
                case END_OBJECT:
                case END_ARRAY: {
                    NodeValue<?> value = parentStack.pop();

                    if (parentStack.isEmpty()) {
                        consumer.accept(value);
                        return;
                    }

                    Object parentValue = parentStack.peek().getValue();
                    if (List.class.isAssignableFrom(parentValue.getClass())) {
                        ((List) parentValue).add(value.getValue());
                        parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    } else if (Set.class.isAssignableFrom(parentValue.getClass())) {
                        ((Set) parentValue).add(value.getValue());
                        parseJsonPayload(parser, parentType, parentStack, methodStack, configuration, consumer);
                    } else if (Map.class.isAssignableFrom(parentValue.getClass())) {
                        ((Map) parentValue).put(methodStack.pop(), value.getValue());
                        parseJsonPayload(parser, parentValue.getClass(), parentStack, methodStack, configuration, consumer);
                    } else if (parentValue.getClass().isArray()) {
                        try {
                            int len = Array.getLength(parentValue);
                            MethodHandle constructor = MethodHandles.arrayConstructor(parentValue.getClass());
                            Object newParentValue = constructor.invoke(len + 1);
                            System.arraycopy(parentValue, 0, newParentValue, 0, len);
                            Array.set(newParentValue, len, value.getValue());
                            //swap parent value in the stack
                            parentStack.pop();
                            parentStack.push(new NodeValue<>(newParentValue));
                            parseJsonPayload(parser, newParentValue.getClass(), parentStack, methodStack, configuration, consumer);
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Object object = parentStack.peek().getValue();
                        Field field = getFieldInfo(object.getClass(), methodStack.peek());
                        invokeSetterHandle(object.getClass(), parentStack, methodStack, value.getValue(), field.getType());
                        parseJsonPayload(parser, object.getClass(), parentStack, methodStack, configuration, consumer);
                    }

                    System.out.println(event);
                    break;
                }
            }
        }
    }

    private static Object invokeNoArgsConstructor(Class<?> type) {
        if (Map.class.equals(type)) {
            //TODO: consider that a user_defined type of map may be specified in the configuration
            return new LinkedHashMap<>();
        } else if (type.isArray()) {
            try {
                MethodHandle constructor = MethodHandles.arrayConstructor(type);
                return constructor.invoke(0);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                MethodHandles.Lookup lookup = MethodHandles.publicLookup();
                MethodType methodType = methodType(void.class);
                MethodHandle constructor = lookup.findConstructor(type, methodType);
                return constructor.invoke();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void invokeSetterHandle(Class<?> parentType, Stack<NodeValue<?>> parentStack, Stack<String> methodStack, Object value, Class<?> valueType) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            String property = methodStack.pop();
            MethodHandle setterMethod = lookup.findVirtual(parentType, String.format("set%s%s", Character.toUpperCase(property.charAt(0)), property.substring(1)), methodType(void.class, valueType));
            setterMethod.invoke(parentStack.peek().getValue(), value);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static Object resolveNumericValue(BigDecimal value, Class<?> targetType) {
        if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
            return value.intValue();
        } else if (targetType.equals(long.class) || targetType.equals(Long.class)) {
            return value.longValue();
        } else if (targetType.equals(double.class) || targetType.equals(Double.class)) {
            return value.doubleValue();
        } else if (targetType.equals(float.class) || targetType.equals(Float.class)) {
            return value.floatValue();
        } else if (targetType.equals(short.class) || targetType.equals(Short.class)) {
            return value.shortValue();
        } else if (targetType.equals(String.class)) {
            return value.toString();
        } else if (targetType.equals(byte.class)) {
            return value.byteValue();
        }
        return value;
    }

    public static Object invokeCollectionConstructor(Class<?> mostLikelyType, Properties configuration, String property) {
        // first, consider any configured property type, else fall back to sensible defaults
        Optional<Class<?>> configuredType = configuredCollectionType(configuration, property);
        if (configuredType.isEmpty()) {
            if (List.class.equals(mostLikelyType)) {
                return new LinkedList<>();
            } else if (Map.class.equals(mostLikelyType)) {
                return new LinkedHashMap<>();
            } else if (Set.class.equals(mostLikelyType)) {
                return new LinkedHashSet<>();
            } else {
                return invokeNoArgsConstructor(mostLikelyType);
            }
        }
        return invokeNoArgsConstructor(configuredType.get());
    }

    private static Optional<Class<?>> configuredCollectionType(Properties configuration, String property) {
        String config = configuration.getProperty(property.isBlank() ? "$" : ("$.") + property);
        Matcher matcher = Pattern.compile("(\\$\\.)?(.*?)\\[.+?]").matcher(config);
        if (matcher.find()) {
            try {
                String className = matcher.group(2);
                return Optional.of(Class.forName(className));
            } catch (ClassNotFoundException e) {
                System.err.printf("Type configured for property '%s' = '(%s)' was not useful. Returning empty match\n", property, matcher.group(2));
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static Optional<String> identityListComponentType(String property, Properties configuration) {
        String config = configuration.getProperty(property.isBlank() ? "$" : ("$.") + property);
        Matcher matcher = Pattern.compile("\\[(.+?)]").matcher(config);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private static Optional<String> identifyConfiguredType(String property, Properties configuration) {
        String config = configuration.getProperty(property.isBlank() ? "$" : ("$.") + property);
        if (config != null) {
            //positive lookahead - match anything coming before the first '<' or '['
            Matcher matcher = Pattern.compile("(.+)(?=[<\\[])").matcher(config);
            if (matcher.find()) {
                return Optional.of(matcher.group(1));
            }
        }
        return Optional.empty();
    }

    private static Optional<String> identityMapComponentType(String property, Properties configuration) {
        String config = configuration.getProperty(property.isBlank() ? "$" : ("$.") + property);
        Matcher matcher = Pattern.compile("<.*,(.+?)>").matcher(config);
        if (matcher.find()) {
            return Optional.of(matcher.group(1).trim());
        }
        return Optional.empty();
    }

    private static Field getFieldInfo(Class<?> parentType, String fieldName) {
        Field field;
        try {
            field = parentType.getDeclaredField(fieldName); //private/protected fields
        } catch (NoSuchFieldException e) {
            try {
                field = parentType.getField(fieldName); //public fields
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }
        return field;
    }

    @Override
    public T apply(InputStream inputStream) {
        try (JsonParser parser = Json.createParser(inputStream)) {
            Properties configuration = new Properties();
            try {
                configuration.load(JNodeParser.class.getClassLoader().getResourceAsStream(configFile));
            } catch (IOException e) {
                System.out.println("No json config properties found");
            }

            final Object[] result = {null};
            parseJsonPayload(parser, type, parentStack, methodStack, configuration,
                    nodeValue -> result[0] = nodeValue.getValue());
            return (T) result[0];
        }
    }
}
