package works.hop.presso.api.extension;

import works.hop.presso.api.plugin.IPlugin;

public interface IExtensionPlugin extends IExtension, IPlugin<IExtension> {

    @Override
    default IExtension find(String identifier) {
        for (IExtension plugin : loader()) {
            if (plugin.contextPath().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
