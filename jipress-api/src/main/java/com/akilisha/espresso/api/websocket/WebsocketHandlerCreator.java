package com.akilisha.espresso.api.websocket;

public abstract class WebsocketHandlerCreator<S> {

    protected OnConnect<S> onConnectHandler;
    protected OnError onErrorHandler;
    protected OnMessage onMessageHandler;
    protected OnBinary onBinaryHandler;
    protected OnClose onCloseHandler;

    public void onConnect(OnConnect<S> handler) {
        this.onConnectHandler = handler;
    }

    public void onError(OnError handler) {
        this.onErrorHandler = handler;
    }

    public void onMessage(OnMessage handler) {
        this.onMessageHandler = handler;
    }

    public void onBinary(OnBinary handler) {
        this.onBinaryHandler = handler;
    }

    public void onClose(OnClose handler) {
        this.onCloseHandler = handler;
    }

    public abstract S getSession();

    public abstract boolean isConnected();

    public abstract boolean isNotConnected();

    public abstract Object getRemote();

    public abstract IWebsocketHandler<S> build();
}
