package works.hop.presso.jett.request;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.server.Request;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.api.request.ReqCookies;
import works.hop.presso.jett.content.BodyParsersCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static works.hop.presso.api.request.ReqCookies.*;

@RequiredArgsConstructor
public class Req implements IRequest {

    final IApplication app;
    final Request request;
    final Map<String, String> params;
    final Map<String, List<String>> query;

    private Map<String, Object> body;

    @Override
    public IApplication app() {
        return this.app;
    }

    @Override
    public String baseUrl() {
        return this.request.getContext().getContextPath();
    }

    @Override
    public <T> T body(Function<byte[], T> converter) {
        try {
            if (this.body == null) {
                IBodyParser parser = BodyParsersCache.parser(request.getContentType());
                byte[] bytes = (byte[]) parser.read(this);
                return converter.apply(bytes);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> body() {
        try {
            if (this.body == null) {
                IBodyParser parser = BodyParsersCache.parser(request.getContentType());
                this.body = parser.read(this, Map.class);
            }
            return this.body;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upload() {
        try {
            IBodyParser parser = BodyParsersCache.parser(request.getContentType());
            parser.read(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReqCookies cookies() {
        ReqCookies cookies = new ReqCookies();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                Map<String, Object> values = new LinkedHashMap<>();
                values.put(NAME, cookie.getName());
                values.put(HTTP_ONLY, cookie.isHttpOnly());
                values.put(COMMENT, cookie.getComment());
                values.put(PATH, cookie.getPath());
                values.put(VERSION, cookie.getVersion());
                values.put(MAX_AGE, cookie.getMaxAge());
                values.put(VALUE, cookie.getValue());
                values.put(DOMAIN, cookie.getDomain());
                cookies.add(values);
            }
        }
        return cookies;
    }

    @Override
    public String hostname() {
        return this.request.getRemoteHost();
    }

    @Override
    public String ip() {
        return this.request.getRemoteAddr();
    }

    @Override
    public String[] ips() {
        return new String[]{this.ip()};
    }

    @Override
    public String method() {
        return this.request.getMethod();
    }

    @Override
    public String originalUrl() {
        return this.request.getOriginalURI();
    }

    @Override
    public String param(String name) {
        if (this.params.containsKey(name)) {
            return this.params.get(name);
        }
        return null;
    }

    @Override
    public <T> T param(String name, Function<String, T> converter) {
        if (this.params.containsKey(name)) {
            return converter.apply(this.params.get(name));
        }
        return null;
    }

    @Override
    public Map<String, String> params() {
        return Collections.unmodifiableMap(this.params);
    }

    @Override
    public String path() {
        return this.request.getPathInfo();
    }

    @Override
    public String protocol() {
        return this.request.getProtocol();
    }

    @Override
    public Map<String, List<String>> query() {
        return Collections.unmodifiableMap(this.query);
    }

    @Override
    public <T> List<T> query(String name, Function<String, T> converter) {
        if (this.query.containsKey(name)) {
            return this.query.get(name).stream().map(converter).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean secure() {
        return this.request.isSecure();
    }

    @Override
    public Boolean signedCookies() {
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public String[] subdomains() {
        String[] splits = request.getRemoteAddr().split("\\.");
        if (splits.length > 2) {
            String[] result = new String[splits.length - 2];
            System.arraycopy(splits, 0, result, 0, result.length);
            return result;
        }
        return new String[0];
    }

    @Override
    public boolean xhr() {
        return this.request.getHeader("X-Requested-With").matches("XMLHttpRequest");
    }

    @Override
    public Boolean accepts(String... types) {
        String header = this.request.getHeader("Accept");
        return header != null && Arrays.stream(types).anyMatch(header::contains);
    }

    @Override
    public String get(String headerName) {
        return this.request.getHeader(headerName);
    }

    @Override
    public Boolean is(String contentType) {
        String header = this.request.getHeader("Content-Type");
        return header != null && header.equals(contentType);
    }

    @Override
    public Object getAttr(String name) {
        return this.request.getAttribute(name);
    }

    @Override
    public void setAttr(String name, Object attr) {
        this.request.setAttribute(name, attr);
    }

    @Override
    public byte[] readSync() throws IOException {
        byte[] bytes = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream str = this.request.getInputStream()) {
            int size;
            while ((size = str.read(bytes)) > -1) {
                baos.write(bytes, 0, size);
            }
        }
        return baos.toByteArray();
    }

    @Override
    public <R> R rawRequest(Class<R> type) {
        return type.cast(this.request);
    }
}
