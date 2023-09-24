package works.hop.presso.jett.content;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import org.eclipse.jetty.server.Request;
import works.hop.presso.api.content.IBodyParser;
import works.hop.presso.api.request.IRequest;
import works.hop.presso.jett.request.Req;

import java.io.File;
import java.io.IOException;

import static works.hop.presso.api.content.IContentType.MULTIPART_FORM_DATA;

public class MultipartFormDataParser implements IBodyParser {
    private final MultipartConfigElement multiPartConfig;

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
        request.setAttr(Request.__MULTIPART_CONFIG_ELEMENT, multiPartConfig);

        // creates the save directory if it does not exists
        File fileSaveDir = new File(multiPartConfig.getLocation());
        if (!fileSaveDir.exists()) {
            assert fileSaveDir.mkdirs();
            assert fileSaveDir.setWritable(true);
        }

        String fileName;
        try {
            for (Part part : ((Req) request).getParts()) {
                fileName = getFileName(part);
                part.write(fileSaveDir.getAbsolutePath() + File.separator + fileName);
            }
        } catch (ServletException e) {
            throw new IOException(e);
        }
        return null;
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
}
