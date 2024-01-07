package works.hop.presso.jett.extension;

import works.hop.presso.api.extension.IExtension;
import works.hop.presso.api.extension.IExtensionPlugin;

import java.util.ServiceLoader;

public class ExtensionPlugins implements IExtensionPlugin {

    private ServiceLoader<IExtension> loader;
    private String contextPath;
    private String resourceRoot;

    public ExtensionPlugins(ServiceLoader<IExtension> loader) {
        this.loader = loader;
    }

    @Override
    public void id(String value) {
        this.contextPath = value;
    }

    @Override
    public String id() {
        return this.contextPath;
    }

    @Override
    public ServiceLoader<IExtension> loader() {
        return this.loader;
    }

    @Override
    public void loader(ServiceLoader<IExtension> loader) {
        this.loader = loader;
    }

    @Override
    public String contextPath() {
        return this.id();
    }

    @Override
    public String resourceRoot() {
        return this.resourceRoot;
    }

    @Override
    public void resourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    @Override
    public <T> void extendWith(T extensionPoint) {
        find(contextPath).extendWith(extensionPoint);
    }
}
