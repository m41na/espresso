package works.hop.presso.api.websocket;

import java.util.List;

public class WebsocketOptionsBuilder {

    private List<String> subProtocols = List.of("protocolOne");
    private String websocketPath = "/";
    private int bufferSize = 128 * 1024;

    private int pulseInterval = 20000;

    private WebsocketOptionsBuilder() {
        //hide constructor
    }

    public static WebsocketOptionsBuilder newBuilder() {
        return new WebsocketOptionsBuilder();
    }

    public WebsocketOptionsBuilder subProtocols(List<String> subProtocols) {
        this.subProtocols = subProtocols;
        return this;
    }

    public WebsocketOptionsBuilder websocketPath(String websocketPath) {
        this.websocketPath = websocketPath;
        return this;
    }

    public WebsocketOptionsBuilder maxBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public WebsocketOptionsBuilder pulseInterval(int interval) {
        this.pulseInterval = interval;
        return this;
    }

    public IWebsocketOptions build() {
        return new IWebsocketOptions() {
            @Override
            public List<String> subProtocols() {
                return WebsocketOptionsBuilder.this.subProtocols;
            }

            @Override
            public String websocketPath() {
                return WebsocketOptionsBuilder.this.websocketPath;
            }

            @Override
            public int maxBufferSize() {
                return WebsocketOptionsBuilder.this.bufferSize;
            }

            @Override
            public int pingInterval() {
                return WebsocketOptionsBuilder.this.pulseInterval;
            }
        };
    }
}
