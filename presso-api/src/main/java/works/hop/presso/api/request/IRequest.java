package works.hop.presso.api.request;

import works.hop.presso.api.application.IApplication;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface IRequest {

    IApplication app();

    String baseUrl();

    <T> T body(Function<byte[], T> converter);

    Map<String, Object> body();

    void upload();

    ReqCookies cookies();

    String hostname();

    String ip();

    String[] ips();

    String method();

    String originalUrl();

    String param(String name);

    <T> T param(String name, Function<String, T> converter);

    Map<String, String> params();

    String path();

    String protocol();

    <T> List<T> query(String name, Function<String, T> converter);

    Map<String, List<String>> query();

    Boolean secure();

    Boolean signedCookies();

    String[] subdomains();

    boolean xhr();

    Boolean accepts(String... types);

    String get(String headerName);

    Boolean is(String contentType);

    Object getAttr(String name);

    void setAttr(String name, Object attr);

    byte[] readSync() throws IOException;
}
