package com.akilisha.espresso.api.plugin;

import com.akilisha.espresso.api.application.IApplication;

public interface IPluginLifecycle {

    void onInitialize(IApplication application);

    void onLoadPlugin(IPluginCallback callback);

    void onReloadPlugin();
}
