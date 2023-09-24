package works.hop.presso.jett.content;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

import java.io.IOException;

import static works.hop.presso.api.content.IContentType.APPLICATION_JSON;

public class JsonBodyParser implements IBodyParser {

    private final JsonProvider provider = Configuration.defaultConfiguration().jsonProvider();

    @Override
    public String contentType() {
        return APPLICATION_JSON;
    }

    @Override
    public Object read(IRequest request) throws IOException {
        byte[] bytes = request.readSync();
        return provider.parse(bytes);
    }

    @Override
    public void write(IResponse response, Object data) {
        String jsonStr = provider.toJson(data);
        response.send(jsonStr);
        response.end();
    }
}
