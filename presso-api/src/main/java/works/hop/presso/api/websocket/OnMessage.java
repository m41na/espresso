package works.hop.presso.api.websocket;

import java.util.function.BiConsumer;

public interface OnMessage<S> extends BiConsumer<S, String> {

}
