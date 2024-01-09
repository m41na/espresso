package com.akilisha.espresso.jett.websocket;

import com.akilisha.espresso.api.websocket.IWebsocketHandler;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class WebsocketHandler extends WebSocketAdapter implements IWebsocketHandler<Session> {

    public RemoteEndpoint getRemote() {
        //delegate to WebSocketAdapter
        return super.getRemote();
    }

    public Session getSession() {
        //delegate to WebSocketAdapter
        return super.getSession();
    }

    public boolean isConnected() {
        //delegate to WebSocketAdapter
        return super.isConnected();
    }

    public boolean isNotConnected() {
        //delegate to WebSocketAdapter
        return super.isNotConnected();
    }

    @Override
    public void onConnect(Session session) {
        //delegate to WebSocketAdapter to cache session
        super.onWebSocketConnect(session);
        //delegate to IWebsocketHandler
        this.onWebSocketConnect(session);
    }

    @Override
    public void onError(Throwable cause) {
        //delegate to IWebsocketHandler
        this.onWebSocketError(cause);
    }

    @Override
    public void onClose(int statusCode, String reason) {
        //delegate to IWebsocketHandler
        this.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onMessage(String message) {
        //delegate to IWebsocketHandler
        this.onWebSocketText(message);
    }

    @Override
    public void onBinary(byte[] payload, int offset, int length) {
        //delegate to IWebsocketHandler
        this.onWebSocketBinary(payload, offset, length);
    }
}
