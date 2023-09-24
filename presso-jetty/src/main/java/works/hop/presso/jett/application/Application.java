package works.hop.presso.jett.application;

import lombok.Getter;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.JettyWebSocketCreator;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.application.CorsOptions;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.middleware.IErrorHandler;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.IParamCallback;
import works.hop.presso.api.router.IRouter;
import works.hop.presso.api.servable.IStaticOptions;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.api.websocket.IWebsocketOptions;
import works.hop.presso.jett.Espresso;
import works.hop.presso.jett.config.ConfigMap;
import works.hop.presso.jett.config.DefaultConfigLoader;
import works.hop.presso.jett.content.BodyParserFactory;
import works.hop.presso.jett.handler.CorsHandler;
import works.hop.presso.jett.router.Router;
import works.hop.presso.jett.view.ViewEngineFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static works.hop.presso.jett.config.ConfigMap.MULTIPART_CONFIG;

@Getter
public class Application extends Router implements IApplication, Cloneable {

    @Getter
    final Map<String, IApplication> subApplications = new HashMap<>();
    final Map<String, Object> local = new HashMap<>();
    final Map<String, Consumer<IApplication>> eventCallbacks = new HashMap<>();
    final Map<String, IParamCallback> pathParamCallbacks = new HashMap<>();
    final List<IErrorHandler> errorHandlers = new LinkedList<>();

    public Application() {
        this.errorHandlers.add(new DefaultErrHandler());
    }

    public ContextHandlerCollection getCtxHandlers() {
        // ideally, only the entry point application should return a valid value
        return null;
    }

    public HandlerList getHandlersList() {
        // ideally, only the entry point application should return a valid value
        return null;
    }

    public AppSettings getSettings() {
        // ideally, only the entry point application should return a valid value
        return null;
    }

    public ConfigMap getAppConfig() {
        // ideally, only the entry point application should return a valid value
        return null;
    }

    @Override
    public Map<String, Object> locals() {
        return this.local;
    }

    @Override
    public String mountPath() {
        return this.getBasePath();
    }

    @Override
    public void on(String event, Consumer<IApplication> callback) {
        this.eventCallbacks.put(event, callback);
    }

    @Override
    public void all(String path, IMiddleware... middlewares) {
        super.all(path, middlewares);
    }

    @Override
    public void disable(String setting) {
        this.getSettings().put(AppSettings.Setting.value(setting), Boolean.FALSE);
    }

    @Override
    public boolean disabled(String setting) {
        return Boolean.parseBoolean(this.getSettings().get(AppSettings.Setting.value(setting)).toString()) == Boolean.FALSE;
    }

    @Override
    public void enable(String setting) {
        this.getSettings().put(AppSettings.Setting.value(setting), Boolean.TRUE);
    }

    @Override
    public boolean enabled(String setting) {
        return Boolean.parseBoolean(this.getSettings().get(AppSettings.Setting.value(setting)).toString()) == Boolean.TRUE;
    }

    @Override
    public void engine(String fileExt, IViewEngine engine) {
        ViewEngineFactory.register(fileExt, engine);
    }

    @Override
    public Object get(String setting) {
        return this.getSettings().get(AppSettings.Setting.value(setting));
    }

    @Override
    public void listen(String host, int port, Consumer<String> callback) throws RuntimeException {
        Espresso.startServer(host, port, callback, this);
    }

    @Override
    public void param(String param, IParamCallback callback) {
        this.pathParamCallbacks.put(param, callback);
    }

    @Override
    public void param(String[] params, IParamCallback callback) {
        for (String param : params) {
            this.param(param, callback);
        }
    }

    @Override
    public void properties(String file) {
        DefaultConfigLoader.load(file, (err, config) -> {
            if (err == null) {
                getAppConfig().put(MULTIPART_CONFIG, config.getMultipart());
            }
        });
    }

    @Override
    public void render(String viewName, BiConsumer<Exception, String> callback) {
        this.render(viewName, Collections.emptyMap(), callback);
    }

    @Override
    public void render(String viewName, Map<String, Object> model, BiConsumer<Exception, String> callback) {
        IViewEngine engine = ViewEngineFactory.engine(
                Objects.requireNonNull(this.getSettings().get(AppSettings.Setting.VIEW_ENGINE),
                        "View engine name must be specified in the app settings").toString(),
                Objects.requireNonNull(this.getSettings().get(AppSettings.Setting.TEMPLATES_DIR),
                        "Templates directory must be specified in the app settings").toString());
        try {
            String content = engine.render(getSettings(), viewName, model);
            callback.accept(null, content);
        } catch (Exception err) {
            callback.accept(err, null);
        }
    }

    @Override
    public IRouter route(String contextPath) {
        //When using this function to create an app, DO NOT use 'app.use()' to mount it. It is already mounted here in the sub-apps
        Application application = new Application();
        application.setBasePath(contextPath);
        this.subApplications.put(contextPath, application);
        return application;
    }

    @Override
    public void set(String setting, Object value) {
        this.getSettings().put(AppSettings.Setting.value(setting), value);
    }

    @Override
    public void use(String usePath, IApplication subApp) {
        ((Router) subApp).setBasePath(usePath);
        this.subApplications.put(usePath, subApp);
        Optional<Consumer<IApplication>> option = Optional.ofNullable(((Application) subApp).eventCallbacks.get("mount"));
        option.ifPresent(consumer -> consumer.accept(this));
    }

    @Override
    public void use(String[] usePaths, IApplication subApp) {
        for (String usePath : usePaths) {
            this.use(usePath, ((Application) subApp).clone());
        }
    }

    @Override
    public void use(CorsOptions options) {
        getHandlersList().addHandler(new CorsHandler(options));
    }

    @Override
    public void use(IMiddleware... handlers) {
        this.all("/[\\w/]+", handlers);
    }

    @Override
    public void use(String path, IMiddleware... handlers) {
        this.all(path, handlers);
    }

    @Override
    public void use(IBodyParser bodyParser) {
        BodyParserFactory.register(bodyParser.contentType(), bodyParser);
    }

    @Override
    public void use(IStaticOptions options) {
        ResourceHandler resourceHandler = Espresso.staticFiles(options);
        getHandlersList().addHandler(resourceHandler);
    }

    @Override
    public void use(String path, IStaticOptions options) {
        ContextHandler contextHandler = Espresso.staticFiles(path, options);
        assert getCtxHandlers() != null;
        getCtxHandlers().addHandler(contextHandler);
    }

    @Override
    public void use(IErrorHandler... handlers) {
        Arrays.asList(handlers).forEach(handler ->
                this.errorHandlers.add(0, handler));
    }

    @Override
    public void websocket(String contextPath, Object creator, IWebsocketOptions options) {
        ServletContextHandler websocketHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        websocketHandler.setContextPath(contextPath);

        JettyWebSocketServletContainerInitializer.configure(websocketHandler, (servletContext, container) -> {
            // Configure default max size
            container.setMaxTextMessageSize(options.maxBufferSize());

            // Add websockets
            container.addMapping(options.websocketPath(), (JettyWebSocketCreator) creator);
        });

        assert getCtxHandlers() != null;
        getCtxHandlers().addHandler(websocketHandler);
    }

    @Override
    public Application clone() {
        try {
            //return shallow clone so that internally the clone is identical to the original
            return (Application) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
