package works.hop.presso.jett.servable;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import works.hop.presso.api.servable.IStaticOptions;

import java.io.IOException;
import java.util.Arrays;

public class StaticOptionsBuilder {

    private String[] welcomeFiles = new String[]{"index.html"};
    private String baseDirectory = "www";
    private boolean acceptRanges = false;
    private boolean listDirectories = false;

    private StaticOptionsBuilder() {
        //hide constructor
    }

    public static StaticOptionsBuilder newBuilder() {
        return new StaticOptionsBuilder();
    }

    public static StaticOptionsBuilder newBuilder(IStaticOptions options) {
        StaticOptionsBuilder bld = newBuilder();
        if (options != null) {
            bld.baseDirectory = options.baseDirectory();
            bld.welcomeFiles = options.welcomeFiles();
            bld.acceptRanges = options.acceptRanges();
            bld.listDirectories = options.listDirectories();
        }
        return bld;
    }

    public StaticOptionsBuilder welcomeFile(String... welcomeFile) {
        this.welcomeFiles = Arrays.stream(welcomeFile).toArray(String[]::new);
        return this;
    }

    public StaticOptionsBuilder acceptRanges(Boolean accept) {
        this.acceptRanges = accept;
        return this;
    }

    public StaticOptionsBuilder listDirectories(boolean list) {
        this.listDirectories = list;
        return this;
    }

    public StaticOptionsBuilder baseDirectory(String directory) {
        this.baseDirectory = directory;
        return this;
    }

    public ResourceHandler build() {
        try {
            ResourceHandler handler = new ResourceHandler();
            // Configure resources base directory
            handler.setBaseResource(Resource.newResource(this.baseDirectory));
            // Configure directory listing.
            handler.setDirectoriesListed(this.listDirectories);
            // Configure welcome files.
            handler.setWelcomeFiles(this.welcomeFiles);
            // Configure whether to accept range requests.
            handler.setAcceptRanges(this.acceptRanges);
            return handler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
