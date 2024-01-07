package com.akilisha.espresso.jett.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import com.akilisha.espresso.api.extension.IExtensionCallback;
import com.akilisha.espresso.api.extension.IExtensionLifecycle;

@Slf4j
public class ExtensionLifecycle implements IExtensionLifecycle<ContextHandlerCollection> {

    IExtensionCallback<ContextHandlerCollection> extensionCallback;

    @Override
    public void onInitialize() {
        log.info("**onInitialize for {}**", ExtensionLifecycle.class.getName());
    }

    @Override
    public void onLoadExtension(IExtensionCallback<ContextHandlerCollection> callback, ContextHandlerCollection context) {
        log.info("**onLoadExtension for {}**", ExtensionLifecycle.class.getName());
        this.extensionCallback = callback;
        this.extensionCallback.onExtend(context);
    }
}
