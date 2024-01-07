package works.hop.presso.api.extension;

public interface IExtension {

    String contextPath();

    String resourceRoot();

    void resourceRoot(String resourceRoot);

    <T> void extendWith(T extensionPoint);
}
