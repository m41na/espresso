package works.hop.presso.api.servable;

public class StaticOptionsBuilder {

    String baseDirectory;
    String[] welcomeFiles = {"index.html"};
    Boolean acceptRanges = true;
    Boolean listDirectories = false;

    private StaticOptionsBuilder() {
        //hide constructor
    }

    public static StaticOptionsBuilder newBuilder() {
        return new StaticOptionsBuilder();
    }

    public StaticOptionsBuilder baseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        return this;
    }

    public StaticOptionsBuilder welcomeFiles(String... welcomeFiles) {
        this.welcomeFiles = welcomeFiles;
        return this;
    }

    public StaticOptionsBuilder acceptRanges(Boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
        return this;
    }

    public StaticOptionsBuilder listDirectories(Boolean listDirectories) {
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
