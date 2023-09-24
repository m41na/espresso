package works.hop.presso.jett.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.io.IOException;

public class Ex0JettyResourceHandler {

    public static void main(String[] args) throws Exception {
        Server server = createServer();

        // Create handlers list
        HandlerList handlerList = new HandlerList();
        handlerList.addHandler(resourceHandler());

        // Create a ContextHandlerCollection to hold contexts.
        ContextHandlerCollection contextCollection = new ContextHandlerCollection();
        handlerList.addHandler(contextCollection);

        // Create the context for the shop web application.
        ContextHandler apiContext = new ContextHandler("/api");
        apiContext.setHandler(new RestfulHandler());
        // Add it to ContextHandlerCollection.
        contextCollection.addHandler(apiContext);

        // Link the HandlerList to the Server.
        server.setHandler(handlerList);

        server.start();
    }

    private static Server createServer() {
        Server server = new Server();

        // The HTTP configuration object.
        HttpConfiguration httpConfig = new HttpConfiguration();
        // Add the SecureRequestCustomizer because we are using TLS.
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        src.setSniHostCheck(false); //NOTE: Only for testing. For PROD, do not set to false
        httpConfig.addCustomizer(src);

        // The ConnectionFactory for HTTP/1.1.
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);

        // The ConnectionFactory for HTTP/2.
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpConfig);

        // The ALPN ConnectionFactory.
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        // The default protocol to use in case there is no negotiation.
        alpn.setDefaultProtocol(http11.getProtocol());

        // Configure the SslContextFactory with the keyStore information.
        String keystorePath = String.format("%s/%s", System.getProperty("user.dir"), "presso-jetty/cert/rizzystore.jks").replaceAll("/", "\\\\");
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(keystorePath);
        sslContextFactory.setKeyStorePassword("s3cret");

        // The ConnectionFactory for TLS.
        SslConnectionFactory tls = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // The ServerConnector instance.
        ServerConnector connector = new ServerConnector(server, tls, alpn, h2, http11);
        connector.setPort(8443);

        server.addConnector(connector);
        return server;
    }

    static ResourceHandler resourceHandler() throws IOException {
        String resourceDir = String.format("%s/%s", System.getProperty("user.dir"), "presso-jetty/view").replaceAll("/", "\\\\");
        // Create and configure a ResourceHandler.
        ResourceHandler handler = new ResourceHandler();
        // Configure the directory where static resources are located.
        handler.setBaseResource(Resource.newResource(resourceDir));
        // Configure directory listing.
        handler.setDirectoriesListed(false);
        // Configure welcome files.
        handler.setWelcomeFiles(new String[]{"index.html"});
        // Configure whether to accept range requests.
        handler.setAcceptRanges(true);

        return handler;
    }

    static class RestfulHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
            baseRequest.setHandled(true);
            // Implement the shop.
            response.getWriter().println("RESTful and grateful");
        }
    }
}
