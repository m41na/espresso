package works.hop.presso.plugin.content.plain;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpHeader;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.response.IResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static works.hop.presso.api.content.IContentType.TEXT_PLAIN;


@Slf4j
public class PlainTextBodyParser implements IBodyParser {

    @Override
    public void init(Map<String, Object> params) {
        log.info("Initializing {}".getClass().getName());
    }

    @Override
    public String contentType() {
        return TEXT_PLAIN;
    }

    @Override
    public Object read(IRequest request) throws IOException {
        ; //allocate bytes for transfer
        return request.readSync();
    }

    @Override
    public void write(IResponse response, Object data) {
        response.append(HttpHeader.CONTENT_TYPE.name(), contentType());
        response.encoding(Charset.defaultCharset());
        response.writeSync(data.toString());
        response.end();
    }
}
