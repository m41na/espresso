package works.hop.presso.jett.application;

import works.hop.presso.api.servable.StaticOptionsBuilder;
import works.hop.presso.api.websocket.WebsocketOptionsBuilder;

import static works.hop.presso.jett.Espresso.express;

public class Ex13AppWebsocketServer {

    public static void main(String[] args) {
        var app = express();
        app.use(StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").welcomeFiles("websocket.html").build());
        app.websocket("/", new Ex13AppEndpointCreator(), WebsocketOptionsBuilder.newBuilder().websocketPath("/events/*").build());

        app.listen(8080);
    }
}
