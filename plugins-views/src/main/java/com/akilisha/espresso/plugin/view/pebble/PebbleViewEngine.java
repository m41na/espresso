package com.akilisha.espresso.plugin.view.pebble;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import com.akilisha.espresso.api.application.AppSettings;
import com.akilisha.espresso.api.view.IViewEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

public class PebbleViewEngine implements IViewEngine {

    private final PebbleEngine engine = new PebbleEngine.Builder().build();
    private String templateDir;

    @Override
    public String name() {
        return PEBBLE;
    }

    @Override
    public void templateDir(String dirName) {
        this.templateDir = dirName;
    }

    @Override
    public String templateDir() {
        return this.templateDir;
    }

    @Override
    public String mergeTemplate(String fileName, Map<String, Object> model) throws IOException {
        String filePath = Path.of(templateDir, fileName).toString();
        PebbleTemplate compiledTemplate = engine.getTemplate(filePath);
        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, model);
        return writer.toString();
    }

    @Override
    public String mergeContent(String content, Map<String, Object> model) throws IOException {
        PebbleTemplate compiledTemplate = engine.getLiteralTemplate(content);
        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, model);
        return writer.toString();
    }

    @Override
    public String render(AppSettings settings, String template, Map<String, Object> model) throws IOException {
        String templateFile = String.format("%s%s", template, settings.get(AppSettings.Setting.TEMPLATES_EXT));
        return mergeTemplate(templateFile, model);
    }
}
