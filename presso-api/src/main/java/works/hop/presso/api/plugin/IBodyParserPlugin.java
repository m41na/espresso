package works.hop.presso.api.plugin;

import works.hop.presso.api.content.IBodyParser;

public interface IBodyParserPlugin extends IBodyParser, IPlugin<IBodyParser> {

    @Override
    default IBodyParser find(String identifier) {
        for (IBodyParser plugin : component()) {
            if (plugin.contentType().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
