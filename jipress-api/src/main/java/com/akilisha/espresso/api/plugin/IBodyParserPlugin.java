package com.akilisha.espresso.api.plugin;

import com.akilisha.espresso.api.content.IBodyParser;

public interface IBodyParserPlugin extends IBodyParser, IPlugin<IBodyParser> {

    @Override
    default IBodyParser find(String identifier) {
        for (IBodyParser plugin : loader()) {
            if (plugin.contentType().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
