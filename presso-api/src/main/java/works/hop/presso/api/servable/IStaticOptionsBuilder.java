package works.hop.presso.api.servable;

import java.io.File;
import java.nio.file.Path;

public class IStaticOptionsBuilder {

    final String projectName = "espresso";
    final String dirProperty = "user.dir";
    String baseDirectory;
    String[] welcomeFiles = {"index.html"};
    Boolean acceptRanges = true;
    Boolean listDirectories = false;

    private IStaticOptionsBuilder() {
        //hide constructor
    }

    public static IStaticOptionsBuilder newBuilder() {
        return new IStaticOptionsBuilder();
    }

    public IStaticOptionsBuilder baseDirectory(String directory) {
        if (new File(directory).exists()) {
            this.baseDirectory = directory;
            return this;
        } else {
            String userDir = System.getProperty(dirProperty);
            String projectPath = userDir.substring(0, userDir.indexOf(projectName));
            Path contentPath = Path.of(projectPath, projectName);
            this.baseDirectory = contentPath.toString();
            return this.baseDirectory(baseDirectory, directory);
        }
    }

    public IStaticOptionsBuilder baseDirectory(String basePath, String directory) {
        Path contentPath = Path.of(basePath, directory);
        this.baseDirectory = contentPath.toString();
        return this;
    }

    public IStaticOptionsBuilder welcomeFiles(String... welcomeFiles) {
        this.welcomeFiles = welcomeFiles;
        return this;
    }

    public IStaticOptionsBuilder acceptRanges(Boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
        return this;
    }

    public IStaticOptionsBuilder listDirectories(Boolean listDirectories) {
        this.listDirectories = listDirectories;
        return this;
    }

    public IStaticOptions build() {
        return new IStaticOptions() {
            @Override
            public String baseDirectory() {
                return baseDirectory;
            }

            @Override
            public String[] welcomeFiles() {
                return welcomeFiles;
            }

            @Override
            public boolean acceptRanges() {
                return acceptRanges;
            }

            @Override
            public boolean listDirectories() {
                return listDirectories;
            }
        };
    }

    public enum DotFiles {ignore, allow, deny}
}
