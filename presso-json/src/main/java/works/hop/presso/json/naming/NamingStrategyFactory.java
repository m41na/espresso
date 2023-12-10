package works.hop.presso.json.naming;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public interface NamingStrategyFactory {

    List<NamingStrategy> registry = new LinkedList<>();

    static void register(NamingStrategy strategy) {
        registry.add(strategy);
    }

    static void forEach(BiConsumer<Integer, NamingStrategy> consumer) {
        IntStream.range(0, registry.size())
                .mapToObj(i -> new Entry<>(i, registry.get(i))).forEach(e -> consumer.accept(e.index(), e.value()));
    }
}
