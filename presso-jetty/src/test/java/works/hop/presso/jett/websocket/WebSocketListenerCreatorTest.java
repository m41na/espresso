package works.hop.presso.jett.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.Test;
import works.hop.presso.api.websocket.IWebsocketHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class WebSocketListenerCreatorTest {

    WebSocketListenerCreator creator = new WebSocketListenerCreator();

    @Test
    void build() {
        Session mockSession = mock(Session.class);
        Throwable mockError = mock(Throwable.class);
        String mockMessage = "very fake message";
        String closeReason = "out for lunch";

        creator.onConnect(session -> {
            assertThat(session).isSameAs(mockSession);
        });

        creator.onError(throwable -> {
            assertThat(throwable).isSameAs(mockError);
        });

        creator.onMessage((session, message) -> {
            assertThat(message).isSameAs(mockMessage);
        });

        creator.onBinary((bytes, offset, length) -> {
            assertThat(new String(bytes)).isEqualTo(mockMessage);
        });

        creator.onClose((status, reason) -> {
            assertThat(reason).isSameAs(closeReason);
        });

        IWebsocketHandler<Session> handler = creator.build();
        handler.onConnect(mockSession);
        handler.onError(mockError);
        handler.onMessage(mockMessage);
        handler.onBinary(mockMessage.getBytes(), 0, 10);
        handler.onClose(1001, closeReason);
    }
}