package works.hop.presso.jett.websocket;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import works.hop.presso.api.websocket.IWebsocketHandler;
import works.hop.presso.api.websocket.WebsocketHandlerCreator;

public class WebSocketListenerCreator extends WebsocketHandlerCreator<Session> {

    private final WebsocketHandler webSocketListener = new WebsocketHandler() {

        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len) {
            WebSocketListenerCreator.this.onBinary.accept(payload, offset, len);
        }

        @Override
        public void onWebSocketText(String message) {
            WebSocketListenerCreator.this.onMessage.accept(getSession(), message);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            WebSocketListenerCreator.this.onClose.accept(statusCode, reason);
        }

        @Override
        public void onWebSocketConnect(Session session) {
            WebSocketListenerCreator.this.onConnect.accept(session);
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            WebSocketListenerCreator.this.onError.accept(cause);
        }
    };

    @Override
    public Session getSession() {
        return webSocketListener.getSession();
    }

    @Override
    public boolean isConnected() {
        return webSocketListener.isConnected();
    }

    @Override
    public boolean isNotConnected() {
        return webSocketListener.isNotConnected();
    }

    @Override
    public RemoteEndpoint getRemote() {
        return webSocketListener.getRemote();
    }

    @Override
    public IWebsocketHandler<Session> build() {
        return this.webSocketListener;
    }
}
