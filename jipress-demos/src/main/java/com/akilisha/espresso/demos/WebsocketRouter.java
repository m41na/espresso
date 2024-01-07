package com.akilisha.espresso.demos;

import org.eclipse.jetty.websocket.api.Session;
import com.akilisha.espresso.api.servable.IStaticOptionsBuilder;
import com.akilisha.espresso.api.websocket.WebsocketOptionsBuilder;
import com.akilisha.espresso.jett.Espresso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

class WebsocketRouter {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.use("/", IStaticOptionsBuilder.newBuilder()
                .baseDirectory("jipress-demos/www")
                .welcomeFiles("web-socket.html")
                .acceptRanges(true)
                .listDirectories(false)
                .build());
        app.websocket("/ws/", WebsocketOptionsBuilder.newBuilder()
                .subProtocols(List.of("protocolOne"))
                .pulseInterval(20000)
                .websocketPath("/events/*")
                .build(), (ws) -> {

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

        app.listen(8888);
    }
}