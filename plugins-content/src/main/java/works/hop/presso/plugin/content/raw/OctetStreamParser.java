package works.hop.presso.plugin.content.raw;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpHeader;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

import java.io.IOException;
import java.util.Map;

import static works.hop.presso.api.content.IContentType.APPLICATION_OCTET_STREAM;

@Slf4j
public class OctetStreamParser implements IBodyParser {

    @Override
    public void init(Map<String, Object> params) {
        log.info("Initializing {}".getClass().getName());
    }

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
        response.append(HttpHeader.CONTENT_TYPE.name(), contentType());
        response.writeSync(data);
        response.end();
    }
}
