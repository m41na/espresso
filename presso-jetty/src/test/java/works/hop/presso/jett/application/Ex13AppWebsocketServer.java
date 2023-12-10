package works.hop.presso.jett.application;

import org.eclipse.jetty.websocket.api.Session;
import works.hop.presso.api.servable.IStaticOptionsBuilder;
import works.hop.presso.api.websocket.WebsocketOptionsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static works.hop.presso.jett.Espresso.express;

public class Ex13AppWebsocketServer {

    public static void main(String[] args) {
        var app = express();
        app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").welcomeFiles("websocket.html").build());
        app.websocket("/ws/", WebsocketOptionsBuilder.newBuilder().subProtocols(List.of("protocolOne"))
                .pulseInterval(20000).websocketPath("/events/*").build(), (ws) -> {

            ws.onConnect(session -> {
                Session sess = (Session) session;
                System.out.println("Socket connected: " + session);
                try {
                    sess.getRemote().sendString("Connection accepted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            ws.onClose((status, reason) -> System.out.printf("Socket closing: %d, %s\n", status, reason));

            ws.onMessage(message -> {
                System.out.println("Received TXT message: " + message);
                if (message.toLowerCase(Locale.ENGLISH).contains("bye")) {
                    ((Session) ws.getSession()).close();
                }
            });

            ws.onBinary((bytes, offset, length) -> {
                System.out.println("Received BYTES message: " + new String(bytes, offset, length));
            });

            ws.onError(cause -> cause.printStackTrace(System.err));
        });

        app.listen(8090);
    }
}
