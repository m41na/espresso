package com.akilisha.espresso.plugin.view.mvel;

import com.akilisha.espresso.api.application.AppSettings;
import com.akilisha.espresso.api.view.IViewEngine;
import org.mvel2.templates.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class MvelViewEngine implements IViewEngine {

    final TemplateRegistry registry = new SimpleTemplateRegistry();
    private String templateDir;

    @Override
    public String name() {
        return MVEL;
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
    public String mergeTemplate(String fileName, Map<String, Object> model) {
        File file = Path.of(this.templateDir, fileName).toFile();
        CompiledTemplate compiled = TemplateCompiler.compileTemplate(file);
        registry.addNamedTemplate(file.getPath(), compiled);
        return (String) TemplateRuntime.execute(compiled, model, registry);
    }

    @Override
    public String mergeContent(String content, Map<String, Object> model) {
        CompiledTemplate compiled = TemplateCompiler.compileTemplate(content);
        return (String) TemplateRuntime.execute(compiled, model, registry);
    }

    @Override
    public String render(AppSettings settings, String template, Map<String, Object> model) {
        String templateFile = String.format("%s%s", template, settings.get(AppSettings.Setting.TEMPLATES_EXT));
        return mergeTemplate(templateFile, model);
    }
}
