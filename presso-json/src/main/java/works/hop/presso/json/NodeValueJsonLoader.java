package works.hop.presso.json;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Stack;

public class NodeValueJsonLoader implements JsonLoader<NodeValue<?>> {

    Stack<NodeValue<?>> parentStack = new Stack<>();
    Stack<String> childStack = new Stack<>();

    @Override
    public NodeValue<?> apply(InputStream inputStream) {
        try (JsonParser parser = Json.createParser(inputStream)) {
            while (parser.hasNext()) {
                final JsonParser.Event event = parser.next();
                switch (event) {
                    case KEY_NAME:
                        childStack.push(parser.getString());
                        System.out.println(childStack.peek());
                        break;
                    case VALUE_STRING: {
                        NodeValue<String> value = new NodeValue<>(parser.getString());
                        System.out.println(value.getValue(String.class));
                        if (parentStack.peek().is(JNodeParser.JArray.class)) {
                            ((JNodeParser.JArray) parentStack.peek().getValue()).add(value);
                        }
                        if (parentStack.peek().is(JNodeParser.JObject.class)) {
                            ((JNodeParser.JObject) parentStack.peek().getValue()).put(childStack.pop(), value);
                        }
                        break;
                    }
                    case VALUE_NUMBER: {
                        NodeValue<BigDecimal> value = new NodeValue<>(parser.getBigDecimal());
                        System.out.println(value.getValue(BigDecimal.class));
                        if (parentStack.peek().is(JNodeParser.JArray.class)) {
                            ((JNodeParser.JArray) parentStack.peek().getValue()).add(value);
                        }
                        if (parentStack.peek().is(JNodeParser.JObject.class)) {
                            ((JNodeParser.JObject) parentStack.peek().getValue()).put(childStack.pop(), value);
                        }
                        break;
                    }
                    case VALUE_TRUE: {
                        NodeValue<Boolean> value = new NodeValue<>(true);
                        System.out.println(value.getValue(Boolean.class));
                        if (parentStack.peek().is(JNodeParser.JArray.class)) {
                            ((JNodeParser.JArray) parentStack.peek().getValue()).add(value);
                        }
                        if (parentStack.peek().is(JNodeParser.JObject.class)) {
                            ((JNodeParser.JObject) parentStack.peek().getValue()).put(childStack.pop(), value);
                        }
                        break;
                    }
                    case VALUE_FALSE: {
                        NodeValue<Boolean> value = new NodeValue<>(false);
                        System.out.println(value.getValue());
                        if (parentStack.peek().is(JNodeParser.JArray.class)) {
                            ((JNodeParser.JArray) parentStack.peek().getValue()).add(value);
                        }
                        if (parentStack.peek().is(JNodeParser.JObject.class)) {
                            ((JNodeParser.JObject) parentStack.peek().getValue()).put(childStack.pop(), value);
                        }
                        break;
                    }
                    case START_OBJECT: {
                        System.out.println(event);
                        parentStack.push(new NodeValue<>(new JNodeParser.JObject()));
                        break;
                    }
                    case START_ARRAY: {
                        System.out.println(event);
                        parentStack.push(new NodeValue<>(new JNodeParser.JArray()));
                        break;
                    }
                    case END_OBJECT:
                    case END_ARRAY: {
                        NodeValue<?> value = parentStack.pop();
                        if (childStack.isEmpty()) {
                            return value;
                        }
                        if (parentStack.peek().is(JNodeParser.JArray.class)) {
                            ((JNodeParser.JArray) parentStack.peek().getValue()).add(value);
                        }
                        if (parentStack.peek().is(JNodeParser.JObject.class)) {
                            ((JNodeParser.JObject) parentStack.peek().getValue()).put(childStack.pop(), value);
                        }
                        System.out.println(event);
                        break;
                    }
                }
            }
        }
        return null;
    }
}
