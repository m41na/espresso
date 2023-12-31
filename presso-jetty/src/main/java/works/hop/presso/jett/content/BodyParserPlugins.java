package works.hop.presso.jett.content;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.plugin.IBodyParserPlugin;
import works.hop.presso.api.request.IRequest;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;

@Slf4j
public class BodyParserPlugins implements IBodyParserPlugin {

    private ServiceLoader<IBodyParser> loader;
    private String contentType;

    public BodyParserPlugins(ServiceLoader<IBodyParser> loader) {
        this.loader = loader;
    }

    @Override
    public ServiceLoader<IBodyParser> loader() {
        return this.loader;
    }

    @Override
    public void loader(ServiceLoader<IBodyParser> loader) {
        this.loader = loader;
    }

    @Override
    public void id(String value) {
        this.contentType = value;
    }

    @Override
    public String id() {
        return this.contentType;
    }

    @Override
    public void init(Map<String, Object> params) {
        log.info("Initializing {} object", getClass().getName());
    }

    @Override
    public String contentType() {
        return this.id();
    }

    @Override
    public Object read(IRequest request) throws IOException {
        return find(request.get("Content-Type")).read(request);
    }
}
