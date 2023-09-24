package works.hop.presso.jett;

import org.eclipse.jetty.http.HttpCompliance;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.*;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.servable.IStaticOptions;
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
import java.util.function.Consumer;
import java.util.stream.Stream;

import static works.hop.presso.api.content.IContentType.*;

public class Espresso {

    public static final String SERVER_NAME = "rizzy-express";
    public static final String DEFAULT_CTX = "/";
    public static final String PATH_SEGMENT = "(\\b.+?)(/)|(\\b.+$)";
    private static final ConfigMap configMap = new ConfigMap();
    private static final AppSettings settings = new AppSettings();
    private static final ContextHandlerCollection ctxHandlers = new ContextHandlerCollection();
    private static final HandlerList handlersList = new HandlerList();

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
            public HandlerList getHandlersList() {
                return handlersList;
            }

            @Override
            public ConfigMap getAppConfig() {
                return configMap;
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

    public static IBodyParser multipart(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold) {
        IBodyParser parser = BodyParserFactory.parser(MULTIPART_FORM_DATA);
        if (parser == null) {
            parser = new MultipartFormDataParser(location, maxFileSize, maxRequestSize, fileSizeThreshold);
            BodyParserFactory.register(MULTIPART_FORM_DATA, parser);
        }
        return parser;
    }

    public static ResourceHandler staticFiles(IStaticOptions options) {
        return StaticOptionsBuilder.newBuilder(options).build();
    }

    public static ContextHandler staticFiles(String context, IStaticOptions options) {
        ContextHandler contextHandler = new ContextHandler(context);
        ResourceHandler handler = StaticOptionsBuilder.newBuilder(options).build();
        contextHandler.setHandler(handler);
        return contextHandler;
    }

    public static void startServer(String host, int port, Consumer<String> callback, IApplication entryApp) {
        try {
            //create thread-pool
            QueuedThreadPool threadPool = new QueuedThreadPool();
            threadPool.setName(SERVER_NAME);
            Server server = new Server(threadPool);

            // The HTTP configuration object.
            HttpConfiguration httpConfig = new HttpConfiguration();
            httpConfig.setHttpCompliance(HttpCompliance.RFC7230); //accept legacy connections
            // The ConnectionFactory for HTTP/1.1.
            HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);

            //create connector
            ServerConnector connector = new ServerConnector(server,
                    Integer.parseInt(((Application) entryApp).getSettings().get(AppSettings.Setting.ACCEPTOR_THREADS).toString()),
                    Integer.parseInt(((Application) entryApp).getSettings().get(AppSettings.Setting.SELECTOR_THREADS).toString()),
                    http11);
            connector.setAcceptQueueSize(Integer.parseInt(((Application) entryApp).getSettings().get(AppSettings.Setting.ACCEPT_QUEUE_SIZE).toString()));
            connector.setHost(host);
            connector.setPort(port);
            server.addConnector(connector);

            // figure out mounted paths
            List<String> mountPaths = new LinkedList<>();
            extractPatterns("/", mountPaths, entryApp);
            Map<String, String> pathPatterns = PathUtils.pathToRegexMap(mountPaths);
            Set<String> pathPrefixes = PathUtils.longestPathPrefix(pathPatterns.values());

            // register context handlers with the context collection
            configureContextHandler(entryApp, pathPrefixes, ctxHandlers);
            for (IApplication application : ((Application) entryApp).getSubApplications().values()) {
                configureContextHandler(application, pathPrefixes, ctxHandlers);
            }

            // add ContextHandlerCollection to handler list
            handlersList.addHandler(ctxHandlers);

            // set default handler
            handlersList.addHandler(new DefaultHandler());

            // add handlers list - invoking handlers up to the first that calls Request.setHandled(true)
            server.setHandler(handlersList);

            //make callback before starting
            callback.accept(String.format("The host %s will now start a server on port %d", host, port));

            //start server
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void configureContextHandler(IApplication application, Set<String> pathPrefixes, ContextHandlerCollection ctxHandlers) {
        String ctxPath = application.mountPath() != null ?
                Stream.of(new String[]{"*", "?", "+"}).anyMatch(ch -> application.mountPath().contains(ch)) ?
                        application.mountPath().replaceAll(PATH_SEGMENT, "") :
                        application.mountPath()
                :
                DEFAULT_CTX;
        if (pathPrefixes.contains(ctxPath)) {
            ContextHandler ctxHandler = new ContextHandler(ctxPath);
            ctxHandler.setHandler(new RouteHandler((Application) application));
            ctxHandlers.addHandler(ctxHandler);
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
