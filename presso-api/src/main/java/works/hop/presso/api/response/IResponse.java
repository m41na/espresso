package works.hop.presso.api.response;

import works.hop.presso.api.application.IApplication;
import works.hop.presso.api.attachment.AttachmentOptions;
import works.hop.presso.api.attachment.AttachmentOptionsBuilder;
import works.hop.presso.api.cookie.CookieOptions;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IResponse {

    int DEFAULT_BUFFER_SIZE = 1024 * 8; //8MB

    IApplication app();

    void append(String header, String value);

    void attachment();

    void attachment(String fileLocation);

    void contentType(String contentType);

    void cookie(String name, String value);

    void cookie(String name, String value, CookieOptions options);

    void clearCookie(String name);

    void clearCookie(String name, CookieOptions options);

    void download(String filePath, String fileName);

    void download(String filePath, String fileName, AttachmentOptions options);

    void download(String filePath, String fileName, AttachmentOptions options, Consumer<Exception> callback);

    void encoding(Charset charset);

    void end();

    void end(Object data, String encoding);

    String get(String headerName);

    void json(Object json);

    void jsonp(Object jsonp);

    void links(Map<String, String> links);

    Map<String, Object> locals();

    void location(String path);

    void redirect(int status, String path);

    void render(String viewName);

    void render(String viewName, Map<String, Object> context);

    void render(String viewName, BiConsumer<Exception, String> consumer);

    void render(String viewName, Map<String, Object> context, BiConsumer<Exception, String> consumer);

    void send(String content);

    void send(Exception err, String content);

    default void sendFile(String filePath) {
        this.sendFile(filePath, AttachmentOptionsBuilder.newBuilder().build(), error -> {
            if (error != null) {
                error.printStackTrace(System.err);
            }
        });
    }

    default void sendFile(String filePath, Consumer<Exception> callback) {
        this.sendFile(filePath, AttachmentOptionsBuilder.newBuilder().build(), callback);
    }

    void sendFile(String filePath, AttachmentOptions options, Consumer<Exception> callback);

    void sendStatus(int status);

    void set(String headerName, String value);

    void status(int status);

    void type(String attachmentType);

    void writeSync(Object content);
}
