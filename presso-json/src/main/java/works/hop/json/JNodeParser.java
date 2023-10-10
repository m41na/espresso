package works.hop.json;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class JNodeParser {

    private static JsonResourceLoader<NodeValue<?>> createNodeValueLoader() {
        return new NodeValueJsonLoader();
    }

    private static <T> JsonResourceLoader<T> createGenericValueLoader(Class<T> type, String configFile) {
        return new GenericValueJsonLoader<>(type, configFile);
    }

    public <T> T parse(String fileName, Class<T> type, String configFile) {
        try {
            JsonResourceLoader<T> jsonResourceLoader = createGenericValueLoader(type, configFile);
            return jsonResourceLoader.classpathResource(fileName);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public NodeValue<?> parse(String fileName) {
        JsonResourceLoader<NodeValue<?>> jsonResourceLoader = createNodeValueLoader();
        return jsonResourceLoader.classpathResource(fileName);
    }

    public static class JObject extends LinkedHashMap<String, NodeValue<?>> {
    }

    public static class JArray extends LinkedList<NodeValue<?>> {
    }
}