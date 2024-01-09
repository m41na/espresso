package com.akilisha.espresso.plugin.content.json;

import com.akilisha.espresso.api.content.IBodyParser;
import com.akilisha.espresso.api.request.IRequest;
import com.akilisha.espresso.api.response.IResponse;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpHeader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static com.akilisha.espresso.api.content.IContentType.APPLICATION_JSON;

@Slf4j
public class JsonBodyParser implements IBodyParser {

    private JsonProvider provider;

    @Override
    public void init(Map<String, Object> params) {
        log.info("Initializing {}".getClass().getName());
        this.provider = Configuration.defaultConfiguration().jsonProvider();
    }

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
        response.append(HttpHeader.CONTENT_TYPE.name(), contentType());
        response.encoding(Charset.defaultCharset());
        response.send(jsonStr);
        response.end();
    }
}
