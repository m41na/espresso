package works.hop.presso.jett.content;

import org.eclipse.jetty.http.HttpHeader;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

import java.io.IOException;

import static works.hop.presso.api.content.IContentType.APPLICATION_OCTET_STREAM;

public class OctetStreamParser implements IBodyParser {

    @Override
    public String contentType() {
        return APPLICATION_OCTET_STREAM;
    }

    @Override
    public Object read(IRequest request) throws IOException {
        return request.readSync();
    }

    @Override
    public void write(IResponse response, Object data) {
        response.append(HttpHeader.CONTENT_TYPE.name(), APPLICATION_OCTET_STREAM);
        response.writeSync(data);
        response.end();
    }
}
