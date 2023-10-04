package works.hop.presso.jett.application;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import works.hop.presso.api.websocket.IWebsocketHandler;

import java.io.IOException;
import java.util.Locale;

public class Ex13AppWebsocketEndpoint extends WebSocketAdapter implements IWebsocketHandler<Session> {

    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("Socket connected: " + session);
        try {
            session.getRemote().sendString("Connection accepted");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Socket closing: " + reason);
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("Received TXT message: " + message);
        if (message.toLowerCase(Locale.ENGLISH).contains("bye")) {
            getSession().close();
        }
    }

    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    @Override
    public void onConnect(Session session) {
        //do nothing
    }

    @Override
    public void onError(Throwable cause) {
        //do nothing
    }

    @Override
    public void onClose(int statusCode, String reason) {
        //do nothing
    }

    @Override
    public void onMessage(String message) {
        //do nothing
    }

    @Override
    public void onBinary(byte[] payload, int offset, int length) {
        //do nothing
    }
}
