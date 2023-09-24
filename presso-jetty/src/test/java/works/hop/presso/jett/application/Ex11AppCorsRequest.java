package works.hop.presso.jett.application;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import works.hop.presso.api.application.CorsOptions;

import static works.hop.presso.jett.Espresso.express;

public class Ex11AppCorsRequest {

    public static void main(String[] args) {
        JsonProvider provider = Configuration.defaultConfiguration().jsonProvider();

        var app = express();
        app.use(new CorsOptions());

        app.get("/", (req, res, next) -> {
            try {
                HttpClient client = new HttpClient();
                client.start();
                ContentResponse response = client.GET("http://localhost:3000/json");
                Object json = provider.parse(response.getContentAsString().trim());
                res.json(json);
                client.stop();
            } catch (Exception e) {
                next.error(e);
            }
        });

        app.listen(3001);
    }
}
