package works.hop.presso.api.application;

public enum StartupEnv {

    SERVER_NAME("serverName", "server name", "jinsi-press"),
    DEPLOY_ENVIRONMENT("deployEnv", "deployment environment", "development"),
    DEV_ENVIRONMENT("development", "production deployment environment", "production"),
    TEST_ENVIRONMENT("test", "test deployment environment", "test"),
    INT_ENVIRONMENT("integration", "integration deployment environment", "integration"),
    STAGE_ENVIRONMENT("staging", "staging deployment environment", "staging"),
    PROD_ENVIRONMENT("production", "production deployment environment", "production"),
    MULTIPART_LOCATION("location", "upload folder for multipart requests. Default is java's temp directory", System.getProperty("java.io.tmpdir")),
    MULTIPART_MAX_FILE_SIZE("maxFileSize", "maximum size permitted for uploaded file. Default is 1MB", 1_000_000L),
    MULTIPART_MAX_REQ_SIZE("maxRequestSize", "maximum size allowed for multipart/form-data request. Default is 10MB", 10_000_000L),
    MULTIPART_FILE_THRESHOLD("fileSizeThreshold", "file size threshold before which it is written to disk. Default is 0", 0),
    SERVER_HOST("host", "server hostname or ipv4 address", "127.0.0.1"),
    SERVER_PORT("port", "server listening port", 3000),
    SERVER_SECURE_PORT("securePort", "server secure listening port", 3443),
    KEYSTORE_PATH("keystorePath", "path to server's keystore", null),
    KEYSTORE_PASS("keystorePass", "password for server's keystore", null),
    SECURE_PROTOCOL("https", "server's secure protocol", "https"),
    REDIRECT_SECURE("redirectSecure", "redirect to secure protocol flag", false);

    public final String property;
    public final String description;
    public final Object value;

    StartupEnv(String property, String description, Object defaultValue) {
        this.property = property;
        this.description = description;
        this.value = defaultValue;
    }

    public static StartupEnv property(String name) {
        for (StartupEnv setting : values()) {
            if (setting.property.equals(name)) {
                return setting;
            }
        }
        throw new RuntimeException("Unknown startup property");
    }
}
