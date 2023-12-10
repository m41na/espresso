package works.hop.presso.json;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class JNodeParser {

    private static JsonLoader<NodeValue<?>> createNodeValueLoader() {
        return new NodeValueJsonLoader();
    }

    private static <T> JsonLoader<T> createGenericValueLoader(Class<T> type, String configFile) {
        return new GenericValueJsonLoader<>(type, configFile);
    }

    public <T> T parse(String fileName, Class<T> type) {
        return this.parse(fileName, type, null);
    }

    public <T> T parse(String fileName, Class<T> type, String configFile) {
        try {
            JsonLoader<T> jsonLoader = createGenericValueLoader(type, configFile);
            return jsonLoader.classpathResource(fileName);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public NodeValue<?> parse(String fileName) {
        JsonLoader<NodeValue<?>> jsonLoader = createNodeValueLoader();
        return jsonLoader.classpathResource(fileName);
    }

    public String stringify(Class<?> type){
        return null;
    }

    public static class JObject extends LinkedHashMap<String, NodeValue<?>> {
    }

    public static class JArray extends LinkedList<NodeValue<?>> {
    }
}