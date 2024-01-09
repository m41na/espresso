package com.akilisha.espresso.api.view;

import com.akilisha.espresso.api.application.AppSettings;

import java.io.IOException;
import java.util.Map;

public interface IViewEngine {

    String MVEL = "mvel";
    String PEBBLE = "pebble";

    String name();

    void templateDir(String dirName);

    String templateDir();

    String mergeTemplate(String fileName, Map<String, Object> model) throws IOException;

    String mergeContent(String content, Map<String, Object> model) throws IOException;

    String render(AppSettings settings, String template, Map<String, Object> model) throws IOException;
}
