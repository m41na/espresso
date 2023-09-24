package works.hop.presso.jett.application;

import org.eclipse.jetty.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;

public class Ex13AppEndpointCreator implements JettyWebSocketCreator {

    @Override
    public Object createWebSocket(JettyServerUpgradeRequest req, JettyServerUpgradeResponse resp) {
        return new Ex13AppWebsocketEndpoint();
    }
}
