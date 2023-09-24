package works.hop.presso.jett.application;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeRequest;
import org.eclipse.jetty.websocket.server.JettyServerUpgradeResponse;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class Ex0JettyWebSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(EventServer.class);

    public static void main(String[] args) throws Exception {
        EventServer eventServer = new EventServer();
        eventServer.setPort(8080);
        eventServer.start();
        eventServer.join();
    }

    public static class EventServer {
        private final Server server;
        private final ServerConnector connector;

        public EventServer() {
            server = new Server();
            connector = new ServerConnector(server);
            server.addConnector(connector);

            // Setup the basic application "context" for this application at "/"
            // This is also known as the handler tree (in jetty speak)
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);

            // Configure specific websocket behavior
            JettyWebSocketServletContainerInitializer.configure(context, (servletContext, wsContainer) ->
            {
                // Configure default max size
                wsContainer.setMaxTextMessageSize(65535);

                // Add websockets
                wsContainer.addMapping("/events/*", new EventEndpointCreator());
            });
        }

        public void setPort(int port) {
            connector.setPort(port);
        }

        public void start() throws Exception {
            server.start();
        }

        public URI getURI() {
            return server.getURI();
        }

        public void stop() throws Exception {
            server.stop();
        }

        public void join() throws InterruptedException {
            LOG.info("Use Ctrl+C to stop server");
            server.join();
        }
    }

    public static class EventEndpoint extends WebSocketAdapter {
        private static final Logger LOG = LoggerFactory.getLogger(EventEndpoint.class);
        private final CountDownLatch closureLatch = new CountDownLatch(1);

        @Override
        public void onWebSocketConnect(Session sess) {
            super.onWebSocketConnect(sess);
            LOG.debug("Endpoint connected: {}", sess);
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
            LOG.debug("Received TEXT message: {}", message);

            if (message.toLowerCase(Locale.US).contains("bye")) {
                getSession().close(StatusCode.NORMAL, "Thanks");
            }
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
            LOG.debug("Socket Closed: [{}] {}", statusCode, reason);
            closureLatch.countDown();
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            super.onWebSocketError(cause);
            cause.printStackTrace(System.err);
        }

        public void awaitClosure() throws InterruptedException {
            LOG.debug("Awaiting closure from remote");
            closureLatch.await();
        }
    }

    public static class EventEndpointCreator implements JettyWebSocketCreator {
        @Override
        public Object createWebSocket(JettyServerUpgradeRequest jettyServerUpgradeRequest, JettyServerUpgradeResponse jettyServerUpgradeResponse) {
            return new EventEndpoint();
        }
    }
}
