package works.hop.presso.api.websocket;

public abstract class WebsocketHandlerCreator<S> {

    protected OnConnect<S> onConnect;
    protected OnError onError;
    protected OnMessage<S> onMessage;
    protected OnBinary onBinary;
    protected OnClose onClose;

    public void onConnect(OnConnect<S> handler) {
        this.onConnect = handler;
    }

    public void onError(OnError handler) {
        this.onError = handler;
    }

    public void onMessage(OnMessage<S> handler) {
        this.onMessage = handler;
    }

    public void onBinary(OnBinary handler) {
        this.onBinary = handler;
    }

    public void onClose(OnClose handler) {
        this.onClose = handler;
    }

    public abstract S getSession();

    public abstract boolean isConnected();

    public abstract boolean isNotConnected();

    public abstract <R> R getRemote();

    public abstract IWebsocketHandler<S> build();
}
