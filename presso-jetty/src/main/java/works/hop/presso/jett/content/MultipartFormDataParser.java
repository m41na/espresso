package works.hop.presso.jett.content;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import org.eclipse.jetty.server.Request;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.jett.request.Req;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static works.hop.presso.api.content.IContentType.MULTIPART_FORM_DATA;

public class MultipartFormDataParser implements IBodyParser {
    private final MultipartConfigElement multiPartConfig;

    public MultipartFormDataParser(){
        this(System.getProperty("java.io.tmpdir"));
    }

    public MultipartFormDataParser(String location) {
        this(location, 10_000_000, 10_000_000, 10_000_000);
    }

    public MultipartFormDataParser(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold) {
        this.multiPartConfig = new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
    }

    @Override
    public String contentType() {
        return MULTIPART_FORM_DATA;
    }

    @Override
    public Object read(IRequest request) throws IOException {
        Map<String, Object> content = new HashMap<>();
        request.setAttr(Request.__MULTIPART_CONFIG_ELEMENT, multiPartConfig);

        // creates the save directory if it does not exist
        File fileSaveDir = new File(multiPartConfig.getLocation());
        if (!fileSaveDir.exists()) {
            assert fileSaveDir.mkdirs();
        }
        // make sure the file is writable
        assert fileSaveDir.setWritable(true);

        String fileName;
        try {
            for (Part part : ((Req) request).getParts()) {
                if (part.getContentType() != null) {
                    fileName = getFileName(part);
                    String savePath = fileSaveDir.getAbsolutePath() + File.separator + fileName;
                    part.write(savePath); // A convenience method to write this uploaded item to disk.
                    content.put(part.getName(), savePath);
                } else {
                    content.put(part.getName(), stringReader(part.getInputStream()));
                }
            }
        } catch (ServletException e) {
            throw new IOException(e);
        }
        return content;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        System.out.println("content-disposition header= " + contentDisposition);
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    private String stringReader(InputStream stream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }
}
