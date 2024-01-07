package com.akilisha.espresso.plugin.content.multipart;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import com.akilisha.espresso.api.application.StartupEnv;
import com.akilisha.espresso.api.content.IBodyParser;
import com.akilisha.espresso.api.request.IRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.akilisha.espresso.api.content.IContentType.MULTIPART_FORM_DATA;

@Slf4j
public class MultipartFormDataParser implements IBodyParser {
    String location = System.getProperty("java.io.tmpdir");
    long maxFileSize = 1_000_000L;
    long maxRequestSize = 10_000_000L;
    int fileSizeThreshold = 0;
    private MultipartConfigElement multiPartConfig;

    @Override
    public void init(Map<String, Object> params) {
        log.info("Initializing {}".getClass().getName());
        this.multiPartConfig = new MultipartConfigElement(
                (String) params.getOrDefault(StartupEnv.MULTIPART_LOCATION.property, location),
                (Long) params.getOrDefault(StartupEnv.MULTIPART_MAX_FILE_SIZE.property, maxFileSize),
                (Long) params.getOrDefault(StartupEnv.MULTIPART_MAX_REQ_SIZE.property, maxRequestSize),
                (int) params.getOrDefault(StartupEnv.MULTIPART_FILE_THRESHOLD.property, fileSizeThreshold));
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
        assert fileSaveDir.exists() || fileSaveDir.mkdirs();
        // make sure the file is writable
        assert fileSaveDir.setWritable(true);

        String fileName;
        try {
            for (Part part : request.rawRequest(Request.class).getParts()) {
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
