package com.akilisha.espresso.api.websocket;

import java.util.List;

public interface IWebsocketOptions {

    List<String> subProtocols();

    String websocketPath();

    int maxBufferSize();

    int pingInterval();
}
