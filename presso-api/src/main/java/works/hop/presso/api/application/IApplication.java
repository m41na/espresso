package works.hop.presso.api.application;

import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.middleware.IErrorHandler;
import works.hop.presso.api.middleware.IMiddleware;
import works.hop.presso.api.middleware.IParamCallback;
import works.hop.presso.api.router.IRouter;
import works.hop.presso.api.servable.IStaticOptions;
import works.hop.presso.api.view.IViewEngine;
import works.hop.presso.api.websocket.IWebsocketOptions;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IApplication extends IRouter {

    Map<String, Object> locals();

    String mountPath();

    void on(String event, Consumer<IApplication> subscriber);

    void all(String path, IMiddleware... middlewares);

    void disable(String setting);

    boolean disabled(String setting);

    void enable(String setting);

    boolean enabled(String setting);

    void engine(IViewEngine engine, String fileExt);

    Object get(String setting);

    default void listen() throws RuntimeException {
        AtomicInteger port = new AtomicInteger();
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            System.out.println("""
                Any code using the port number returned by this method is subject to a race condition - a different\s
                process/thread may bind to the same port immediately after the ServerSocket instance is closed.
                """);
            port.set(serverSocket.getLocalPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listen(port.get());
    }

    default void listen(int port) throws RuntimeException {
        listen("localhost", port, System.out::println);
    }

    void listen(String host, int port, Consumer<String> callback) throws RuntimeException;

    void param(String param, IParamCallback callback);

    void param(String[] params, IParamCallback callback);

    void properties(String file);

    void render(String viewName, BiConsumer<Exception, String> callback);

    void render(String viewName, Map<String, Object> model, BiConsumer<Exception, String> callback);

    IRouter route(String contextPath);

    void set(String setting, Object value);

    void use(String usePath, IApplication subApp);

    void use(String[] usePaths, IApplication subApp);

    void use(CorsOptions options);

    void use(IMiddleware... handlers);

    void use(String path, IMiddleware... handlers);

    void use(IBodyParser bodyParser);

    void use(IStaticOptions options);

    void use(String path, IStaticOptions options);

    void use(IErrorHandler... handlers);

    void websocket(String contextPath, Object creator, IWebsocketOptions options);
}
