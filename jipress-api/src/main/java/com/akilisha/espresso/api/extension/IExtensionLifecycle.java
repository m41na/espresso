package com.akilisha.espresso.api.extension;

public interface IExtensionLifecycle<T> {

    void onInitialize();

    void onLoadExtension(IExtensionCallback<T> callback, T extensionPoint);
}
