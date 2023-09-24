package works.hop.presso.jett.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.Callback;

import java.io.IOException;

public class Ex0JettyContextHandlers {

    public static void main(String[] args) throws Exception {
        Server server = createServer();

        //2. Handlers
        // Create a ContextHandlerCollection to hold contexts.
        ContextHandlerCollection contextCollection = new ContextHandlerCollection();
        // Link the ContextHandlerCollection to the Server.
        server.setHandler(contextCollection);

        // Create the context for the shop web application.
        ContextHandler shopContext = new ContextHandler("/shop");
        shopContext.setHandler(new ShopHandler());
        // Add it to ContextHandlerCollection.
        contextCollection.addHandler(shopContext);

        // Create the context for the API web application.
        ContextHandler apiContext = new ContextHandler("/api");
        apiContext.setHandler(new RESTHandler());
        // Web applications can be deployed after the Server is started.
        contextCollection.deployHandler(apiContext, Callback.NOOP);

        server.start();
    }

    private static Server createServer() {
        Server server = new Server();

        //1. Connector
        // The HTTP configuration object.
        HttpConfiguration httpConfig = new HttpConfiguration();
        // Configure the HTTP support, for example:
        httpConfig.setSendServerVersion(false);

        // The ConnectionFactory for HTTP/1.1.
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);

        // The ConnectionFactory for the PROXY protocol.
        ProxyConnectionFactory proxy = new ProxyConnectionFactory(http11.getProtocol());

        // Create the ServerConnector.
        ServerConnector connector = new ServerConnector(server, proxy, http11);
        connector.setPort(8080);

        server.addConnector(connector);
        return server;
    }

    static class ShopHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            baseRequest.setHandled(true);
            // Implement the shop.
            response.getWriter().println("Shopping always");
        }
    }

    static class RESTHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            baseRequest.setHandled(true);
            // Implement the REST APIs.
            response.getWriter().println("REST API always");
        }
    }
}
