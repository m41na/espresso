package works.hop.presso.api.websocket;

public interface IWebsocketHandler<S> {

    void onConnect(S session);

    void onError(Throwable cause);

    void onClose(int statusCode, String reason);

    void onMessage(String message);

    void onBinary(byte[] payload, int offset, int length);
}
