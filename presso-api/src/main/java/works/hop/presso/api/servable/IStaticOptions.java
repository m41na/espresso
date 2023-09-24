package works.hop.presso.api.servable;

public interface IStaticOptions {

    String baseDirectory();

    String[] welcomeFiles();

    boolean acceptRanges();

    boolean listDirectories();
}
