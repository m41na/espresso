package works.hop.presso.api.websocket;

public interface OnBinary {

    void accept(byte[] payload, int offset, int length);
}
