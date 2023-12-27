package works.hop.presso.jett;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http.HttpCompliance;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.servable.IStaticOptions;
import works.hop.presso.cli.StartUp;
import works.hop.presso.jett.application.Application;
import works.hop.presso.jett.application.PathUtils;
import works.hop.presso.jett.config.ConfigMap;
import works.hop.presso.jett.content.*;
import works.hop.presso.jett.handler.RouteHandler;
import works.hop.presso.jett.servable.StaticOptionsBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static works.hop.presso.api.content.IContentType.*;

public class Espresso {

    public static final String SERVER_NAME = "kahawa-press";
    public static final String DEFAULT_CTX = "/";
    public static final String PATH_SEGMENT = "(\\b.+?)(/)|(\\b.+$)";
    private static final ConfigMap configMap = new ConfigMap();
    private static final AppSettings settings = new AppSettings();
    private static final ContextHandlerCollection ctxHandlers = new ContextHandlerCollection();
    private static final HandlerList handlerList = new HandlerList();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private Espresso() {
        //hidden
    }

    public static IApplication express() {

        return new Application() {

            @Override
            public AppSettings getSettings() {
                return settings;
            }

            @Override
            public ContextHandlerCollection getCtxHandlers() {
                return ctxHandlers;
            }

            @Override
            public HandlerList getHandlerList() {
                return handlerList;
            }

            @Override
            public ConfigMap getAppConfig() {
                return configMap;
            }

            @Override
            public ScheduledExecutorService getExecutorService() {
                return scheduler;
            }
        };
    }

    public static IBodyParser json() {
        IBodyParser parser = BodyParserFactory.parser(APPLICATION_JSON);
        if (parser == null) {
            parser = new JsonBodyParser();
            BodyParserFactory.register(APPLICATION_JSON, parser);
        }
        return parser;
    }

    public static IBodyParser raw() {
        IBodyParser parser = BodyParserFactory.parser(APPLICATION_OCTET_STREAM);
        if (parser == null) {
            parser = new OctetStreamParser();
            BodyParserFactory.register(APPLICATION_OCTET_STREAM, parser);
        }
        return parser;
    }

    public static IBodyParser text() {
        IBodyParser parser = BodyParserFactory.parser(TEXT_PLAIN);
        if (parser == null) {
            parser = new PlainTextBodyParser();
            BodyParserFactory.register(TEXT_PLAIN, parser);
        }
        return parser;
    }

    public static IBodyParser urlEncoded() {
        IBodyParser parser = BodyParserFactory.parser(FORM_URL_ENCODED);
        if (parser == null) {
            parser = new FormUrlEncodedParser();
            BodyParserFactory.register(FORM_URL_ENCODED, parser);
        }
        return parser;
    }

    public static IBodyParser multipart(String location) {
        IBodyParser parser = BodyParserFactory.parser(MULTIPART_FORM_DATA);
        if (parser == null) {
            parser = new MultipartFormDataParser(location);
            BodyParserFactory.register(MULTIPART_FORM_DATA, parser);
        }
        return parser;
    }

    public static IBodyParser multipart(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold) {
        IBodyParser parser = BodyParserFactory.parser(MULTIPART_FORM_DATA);
        if (parser == null) {
            parser = new MultipartFormDataParser(location, maxFileSize, maxRequestSize, fileSizeThreshold);
            BodyParserFactory.register(MULTIPART_FORM_DATA, parser);
        }
        return parser;
    }

    public static ResourceHandler staticFiles(IStaticOptions options) {
        return StaticOptionsBuilder.newBuilder().options(options).build();
    }

    public static ContextHandler staticFiles(String context, IStaticOptions options) {
        ContextHandler contextHandler = new ContextHandler(context);
        ResourceHandler handler = StaticOptionsBuilder.newBuilder().options(options).build();
        contextHandler.setHandler(handler);
        return contextHandler;
    }

    public static Server create(String host, int port, IApplication entryApp) {
        //extract startup options
        StartUp props = StartUp.instance();

        // extract startup values
        String hostName = props.getOrDefault("host", host);
        int httpPort = props.getOrDefault("port", port, Integer::parseInt);
        int httpsPort = props.getOrDefault("securePort", 443, Integer::parseInt);
        String certPath = props.getOrDefault("keystorePath", props.cacertsPath());
        String certPass = props.getOrDefault("keystorePass", props.defaultPass());

        //create thread-pool
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName(SERVER_NAME);
        Server server = new Server(threadPool);
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
        // apply the connectors
        server.setConnectors(new Connector[]{
                createHttpConnector(hostName, httpPort, httpsPort, server, entryApp),
                createHttpsConnector(httpsPort, certPath, certPass, server)
        });

        return server;
    }

    private static ServerConnector createHttpConnector(String host, int httpPort, int httpsPort, Server server, IApplication entryApp) {
        // The HTTP configuration object.
        HttpConfiguration httpConfig = createHttpConfiguration(httpsPort);

        // The ConnectionFactory for HTTP/1.1.
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);
        // create the connector:
        ServerConnector httpConnector = new ServerConnector(server,
                Integer.parseInt(((Application) entryApp).getSettings().get(AppSettings.Setting.ACCEPTOR_THREADS).toString()),
                Integer.parseInt(((Application) entryApp).getSettings().get(AppSettings.Setting.SELECTOR_THREADS).toString()),
                http11);
        httpConnector.setAcceptQueueSize(Integer.parseInt(((Application) entryApp).getSettings().get(AppSettings.Setting.ACCEPT_QUEUE_SIZE).toString()));

        // set the connector's port:
        httpConnector.setHost(host);
        httpConnector.setPort(httpPort);

        return httpConnector;
    }

    private static ServerConnector createHttpsConnector(int httpsPort, String certPath, String certPass, Server server) {
        // The HTTP configuration object.
        HttpConfiguration httpConfig = createHttpConfiguration(httpsPort);
        httpConfig.addCustomizer(createSecureRequestCustomizer());

        // HTTP/1.1:
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);
        // HTTP/2 connections:
        HTTP2ServerConnectionFactory http2 = new HTTP2ServerConnectionFactory(httpConfig);
        // ALPN - a TLS extension:
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol(http11.getProtocol()); // fallback protocol

        // add SSL/TLS:
        SslContextFactory.Server sslContextFactory = getSslContextFactory(certPath, certPass);
        SslConnectionFactory tls = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());
        DetectorConnectionFactory conFactory = new DetectorConnectionFactory(tls);
        // create the connector:
        ServerConnector httpsConnector = new ServerConnector(server, conFactory, alpn, http2, http11);
        // set the connector's port:
        httpsConnector.setPort(httpsPort);

        return httpsConnector;
    }

    private static HttpConfiguration createHttpConfiguration(int httpsPort) {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSecurePort(httpsPort);
        httpConfig.setSecureScheme("https");
        httpConfig.setHttpCompliance(HttpCompliance.RFC7230);
        return httpConfig;
    }

    private static SecureRequestCustomizer createSecureRequestCustomizer() {
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        // customize for additional configuration
        src.setSniHostCheck(false); //NOTE: Only for testing. For PROD, do not set to false
        return src;
    }

    private static SslContextFactory.Server getSslContextFactory(String certPath, String certPass) {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(certPath);
        sslContextFactory.setKeyStorePassword(certPass);
        return sslContextFactory;
    }

    public static void startServer(String host, int port, Consumer<String> callback, IApplication entryApp) {
        try {
            Server server = create(host, port, entryApp);

            // figure out mounted paths
            List<String> mountPaths = new LinkedList<>();
            extractPatterns("/", mountPaths, entryApp);
            Map<String, String> pathPatterns = PathUtils.pathToRegexMap(mountPaths);
            Set<String> pathPrefixes = PathUtils.longestPathPrefix(pathPatterns.values());

            // register context handlers with the context collection
            configureContextHandler(entryApp, pathPrefixes);
            for (IApplication application : ((Application) entryApp).getSubApplications().values()) {
                configureContextHandler(application, pathPrefixes);
            }

            // if using secure protocol, rewrite url to https
            Boolean redirectSecure = StartUp.instance().getOrDefault("redirectSecure", false, Boolean::parseBoolean);
            if (redirectSecure) {
                handlerList.addHandler(new SecuredRedirectHandler());
            }

            // add ContextHandlerCollection to the handler list
            handlerList.addHandler(ctxHandlers);

            // set default handler
            handlerList.addHandler(new DefaultHandler());

            // add handlers list - invoking handlers up to the first that calls Request.setHandled(true)
            server.setHandler(handlerList);

            //make callback before starting
            callback.accept(String.format("The host %s will now start a server on port %d", host, port));

            //start server
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void configureContextHandler(IApplication application, Set<String> pathPrefixes) {
        String ctxPath = application.mountPath() != null ?
                Stream.of(new String[]{"*", "?", "+"}).anyMatch(ch -> application.mountPath().contains(ch)) ?
                        application.mountPath().replaceAll(PATH_SEGMENT, "") :
                        application.mountPath()
                :
                DEFAULT_CTX;
        if (pathPrefixes.contains(ctxPath)) {
            ContextHandler ctxHandler = new ContextHandler(ctxPath);
            ctxHandler.setHandler(new RouteHandler((Application) application));
            Espresso.ctxHandlers.addHandler(ctxHandler);
            pathPrefixes.remove(ctxPath);
        }
    }

    private static void extractPatterns(String prefix, List<String> hierarchy, IApplication application) {
        String newPrefix;
        if (application.mountPath() == null) {
            newPrefix = (prefix + DEFAULT_CTX).replaceAll("/+", "/");
            hierarchy.add(newPrefix);

            for (IApplication app : ((Application) application).getSubApplications().values()) {
                extractPatterns(newPrefix, hierarchy, app);
            }
        } else {
            newPrefix = (prefix + application.mountPath()).replaceAll("/+", "/");
            hierarchy.add(newPrefix);

            for (IApplication app : ((Application) application).getSubApplications().values()) {
                extractPatterns(newPrefix, hierarchy, app);
            }
        }
    }
}
