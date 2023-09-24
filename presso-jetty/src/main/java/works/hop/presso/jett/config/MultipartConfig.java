package works.hop.presso.jett.config;

import lombok.Data;

@Data
public class MultipartConfig {
    String location = "app-default.yaml";
    long maxFileSize = 10_000_000L;
    long maxRequestSize = 10_000_000L;
    int fileSizeThreshold = 10_000_000;
}
