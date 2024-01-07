package com.akilisha.espresso.jett.application;

import lombok.Getter;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import com.akilisha.espresso.api.application.AppSettings;
import com.akilisha.espresso.api.application.CorsOptions;
import com.akilisha.espresso.api.application.IApplication;
import com.akilisha.espresso.api.content.IBodyParser;
import com.akilisha.espresso.api.middleware.IErrorHandler;
import com.akilisha.espresso.api.middleware.IMiddleware;
import com.akilisha.espresso.api.middleware.IParamCallback;
import com.akilisha.espresso.api.router.IRouter;
import com.akilisha.espresso.api.servable.IStaticOptions;
import com.akilisha.espresso.api.view.IViewEngine;
import com.akilisha.espresso.api.websocket.IWebsocketOptions;
import com.akilisha.espresso.api.websocket.WebsocketHandlerCreator;
import com.akilisha.espresso.jett.Espresso;
import com.akilisha.espresso.jett.config.ConfigMap;
import com.akilisha.espresso.jett.config.DefaultConfigLoader;
import com.akilisha.espresso.jett.content.BodyParsersCache;
import com.akilisha.espresso.jett.handler.CorsHandler;
import com.akilisha.espresso.jett.router.Router;
import com.akilisha.espresso.jett.view.ViewEnginesCache;
import com.akilisha.espresso.jett.websocket.WebSocketListenerCreator;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.akilisha.espresso.jett.config.ConfigMap.MULTIPART_CONFIG;

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

    public HandlerList getHandlerList() {
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

    public ScheduledExecutorService getExecutorService() {
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
    public void engine(String engine, String templateDir, String fileExt) {
        this.set(AppSettings.Setting.TEMPLATES_DIR.property, templateDir);
        this.set(AppSettings.Setting.VIEW_ENGINE.property, engine);
        this.set(AppSettings.Setting.TEMPLATES_EXT.property, fileExt);
    }

    @Override
    public void engine(IViewEngine engine, String fileExt) {
        this.set(AppSettings.Setting.TEMPLATES_DIR.property, engine.templateDir());
        this.set(AppSettings.Setting.VIEW_ENGINE.property, engine.name());
        this.set(AppSettings.Setting.TEMPLATES_EXT.property, fileExt);
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
        IViewEngine engine = ViewEnginesCache.engine(
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
        getHandlerList().addHandler(new CorsHandler(options));
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
        BodyParsersCache.register(bodyParser.contentType(), bodyParser);
    }

    @Override
    public void use(IStaticOptions options) {
        ResourceHandler resourceHandler = Espresso.staticFiles(options);
        getHandlerList().addHandler(resourceHandler);
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
    public void websocket(String contextPath, IWebsocketOptions options, Consumer<WebsocketHandlerCreator<?>> creator) {
        ServletContextHandler websocketHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        websocketHandler.setContextPath(contextPath);

        JettyWebSocketServletContainerInitializer.configure(websocketHandler, (servletContext, container) -> {
            // Configure default max size
            container.setMaxTextMessageSize(options.maxBufferSize());

            // Add websockets
            container.addMapping(options.websocketPath(), (upgradeRequest, upgradeResponse) -> {

                //possible to inspect upgrade request and modify upgrade response
                upgradeResponse.setAcceptedSubProtocol(options.subProtocols().get(0));

                //provider a builder for websocket endpoints
                WebSocketListenerCreator handlerCreator = new WebSocketListenerCreator(getExecutorService(), options.pingInterval());
                creator.accept(handlerCreator);

                //return a websocket endpoint
                return handlerCreator.build();
            });
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
