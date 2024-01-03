package works.hop.presso.jett.response;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.attachment.AttachmentOptions;
import works.hop.presso.api.attachment.AttachmentOptionsBuilder;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.content.IMimeType;
import works.hop.presso.api.cookie.CookieOptions;
import works.hop.presso.api.response.IResponse;
import works.hop.presso.jett.content.BodyParsersCache;
import works.hop.presso.jett.cookie.CookieBuilder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static works.hop.presso.api.content.IContentType.APPLICATION_JSON;

@RequiredArgsConstructor
public class Res implements IResponse {

    final IApplication app;
    final Request request;
    final Response response;
    String attachmentType = ".html";
    Map<String, Object> local = new HashMap<>();

    @Override
    public IApplication app() {
        return this.app;
    }

    @Override
    public void append(String header, String value) {
        this.response.setHeader(header, value);
    }

    @Override
    public void attachment() {
        // Content-Disposition: attachment
        response.setHeader("Content-Disposition", "attachment");
    }

    @Override
    public void attachment(String fileName) {
        // Content-Disposition: attachment; filename="logo.png"
        // Content-Type: image/png
        String ext = fileName.substring(fileName.lastIndexOf("."));
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setHeader(HttpHeader.CONTENT_TYPE.name(), IMimeType.mapping.get(ext));
    }

    @Override
    public void contentType(String contentType) {
        this.response.setContentType(contentType);
    }

    @Override
    public void cookie(String name, String value) {
        this.response.addCookie(new Cookie(name, value));
    }

    @Override
    public void cookie(String name, String value, CookieOptions options) {
        this.response.addCookie(CookieBuilder.newBuilder(name, value, options)
                .maxAge((TimeUnit) options.getOrDefault(CookieOptions.Option.TIME_UNIT, TimeUnit.MINUTES),
                        (Integer) options.getOrDefault(CookieOptions.Option.MAX_AGE, 30))
                .secure(false)
                .build(request));
    }

    @Override
    public void clearCookie(String name) {
        Cookie cookie = new Cookie(name, null); // Not necessary, but saves bandwidth.
        cookie.setMaxAge(0); // Don't set to -1, or it will become a session cookie!
        this.response.addCookie(cookie);
    }

    @Override
    public void clearCookie(String name, CookieOptions options) {
        Cookie cookie = CookieBuilder.newBuilder(name, null, options).build(request);
        cookie.setMaxAge(0); // Don't set to -1, or it will become a session cookie!
        this.response.addCookie(cookie);
    }

    @Override
    public void download(String filePath, String fileName) {
        this.download(filePath, fileName, AttachmentOptionsBuilder.newBuilder().build());
    }

    @Override
    public void download(String filePath, String fileName, AttachmentOptions options) {
        this.download(filePath, fileName, AttachmentOptionsBuilder.newBuilder().build(), error -> {
            if (error != null) {
                error.printStackTrace(System.err);
            }
        });
    }

    @Override
    public void download(String filePath, String fileName, AttachmentOptions options, Consumer<Exception> callback) {
        this.attachment(fileName);
        File file = Path.of(filePath, fileName).toFile();
        this.writeFile(file, options, callback);
    }

    @Override
    public void encoding(Charset charset) {
        this.response.setCharacterEncoding(charset.name());
    }

    @Override
    public void end() {
        this.request.setHandled(true);
    }

    @Override
    public void end(Object data, String encoding) {
        this.response.setCharacterEncoding(encoding);
        this.writeSync(data);
        this.end();
    }

    @Override
    public String get(String headerName) {
        return response.getHeader(headerName);
    }

    @Override
    public void json(Object json) {
        IBodyParser parser = BodyParsersCache.parser(APPLICATION_JSON);
        parser.write(this, json);
    }

    @Override
    public void jsonp(Object jsonp) {
        this.json(jsonp);
    }

    @Override
    public void links(Map<String, String> links) {
        this.json(links);
    }

    @Override
    public Map<String, Object> locals() {
        return this.local;
    }

    @Override
    public void location(String path) {
        this.redirect(302, path);
    }

    @Override
    public void redirect(int status, String path) {
        this.status(301);
        this.set("Location", path);
        this.send("");
    }

    @Override
    public void render(String viewName) {
        this.app.render(viewName, Collections.emptyMap(), this::send);
    }

    @Override
    public void render(String viewName, BiConsumer<Exception, String> consumer) {
        this.app.render(viewName, Collections.emptyMap(), consumer);
    }

    @Override
    public void render(String viewName, Map<String, Object> context, BiConsumer<Exception, String> consumer) {
        this.app.render(viewName, context, consumer);
    }

    @Override
    public void render(String viewName, Map<String, Object> context) {
        this.app.render(viewName, context, (err, content) -> {
            if (err != null) {
                status(500);
                send(err.getMessage());
            } else {
                status(200);
                send(content);
            }
        });
    }

    @Override
    public void send(String content) {
        this.writeSync(content);
        this.end();
    }

    @Override
    public void send(Exception err, String content) {
        if (err != null) {
            this.status(500);
            this.send(err.getMessage());
            return;
        }
        this.status(200);
        this.send(content);
    }

    @Override
    public void sendFile(String filePath, AttachmentOptions options, Consumer<Exception> callback) {
        this.contentType(filePath.substring(filePath.lastIndexOf(".")));
        File file = new File(filePath);
        this.writeFile(file, options, callback);
    }

    private void writeFile(File file, AttachmentOptions options, Consumer<Exception> callback) {
        try (InputStream in = new FileInputStream(file);
             OutputStream out = this.response.getOutputStream()) {

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        } catch (IOException e) {
            callback.accept(e);
        }
    }

    @Override
    public void sendStatus(int status) {
        this.response.setStatus(status);
        this.send("");
    }

    @Override
    public void set(String headerName, String value) {
        this.response.setHeader(headerName, value);
    }

    @Override
    public void status(int status) {
        this.response.setStatus(status);
    }

    @Override
    public void type(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    @Override
    public void writeSync(Object content) {
        try {
            PrintWriter printer = this.response.getWriter();
            printer.println(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
