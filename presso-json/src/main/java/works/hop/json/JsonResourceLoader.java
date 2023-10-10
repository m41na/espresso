package works.hop.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

public interface JsonResourceLoader<T> extends Function<InputStream, T> {

    default T classpathResource(String fileName) {
        try (InputStream is = Objects.requireNonNull(
                JNodeParser.class.getClassLoader().getResourceAsStream(fileName))) {
            return apply(is);
        } catch (IOException e) {
            try (InputStream is = Objects.requireNonNull(
                    JNodeParser.class).getResourceAsStream(fileName)) {
                return apply(is);
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
}
