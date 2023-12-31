package works.hop.presso.jett.content;

import works.hop.presso.api.content.IBodyParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class BodyParserFactory {

    public static Map<String, IBodyParser> parsers = new LinkedHashMap<>();

    private BodyParserFactory() {
        //hide constructor
    }

    public static void register(String contentType, IBodyParser parser) {
        parsers.putIfAbsent(contentType, parser);
    }

    public static IBodyParser parser(String contentType) {
        if (parsers.containsKey(contentType)) {
            return parsers.get(contentType);
        }
        throw new NullPointerException(String.format("There is no content parser configured for the type %s", contentType));
    }
}
