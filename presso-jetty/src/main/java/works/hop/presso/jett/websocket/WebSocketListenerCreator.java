package works.hop.presso.jett.websocket;

import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import works.hop.presso.api.websocket.IWebsocketHandler;
import works.hop.presso.api.websocket.WebsocketHandlerCreator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class WebSocketListenerCreator extends WebsocketHandlerCreator<Session> {

    private final ScheduledExecutorService scheduler;
    private final int pingInterval;

    private final WebsocketHandler webSocketListener = new WebsocketHandler() {

        private ScheduledFuture<?> heartBeat;

        @Override
        public void onWebSocketBinary(byte[] payload, int offset, int len) {
            WebSocketListenerCreator.this.onBinaryHandler.accept(payload, offset, len);
            this.heartBeat.cancel(true);
            this.heartBeat = schedulePingEvent();
        }

        @Override
        public void onWebSocketText(String message) {
            WebSocketListenerCreator.this.onMessageHandler.accept(message);
            this.heartBeat.cancel(true);
            this.heartBeat = schedulePingEvent();
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            WebSocketListenerCreator.this.onCloseHandler.accept(statusCode, reason);
            this.heartBeat.cancel(true);
            getSession().close(statusCode, reason);
        }

        @Override
        public void onWebSocketConnect(Session session) {
            super.onWebSocketConnect(session); //cache the session object
            WebSocketListenerCreator.this.onConnectHandler.accept(session);
            this.heartBeat = schedulePingEvent();
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            WebSocketListenerCreator.this.onErrorHandler.accept(cause);
        }

        private ScheduledFuture<?> schedulePingEvent() {
            return WebSocketListenerCreator.this.scheduler.scheduleAtFixedRate(() -> {
                try {
                    getRemote().sendPing(ByteBuffer.wrap("ping".getBytes()));
                } catch (IOException e) {
                    onWebSocketClose(3000, "Could not reach client");
                }
            }, pingInterval, pingInterval, TimeUnit.MILLISECONDS);
        }
    };

    @Override
    public Session getSession() {
        return this.webSocketListener.getSession();
    }

    @Override
    public boolean isConnected() {
        return this.webSocketListener.isConnected();
    }

    @Override
    public boolean isNotConnected() {
        return this.webSocketListener.isNotConnected();
    }

    @Override
    public RemoteEndpoint getRemote() {
        return this.webSocketListener.getRemote();
    }

    @Override
    public IWebsocketHandler<Session> build() {
        return this.webSocketListener;
    }
}
