package works.hop.presso.view;

import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.plugin.IViewEnginePlugin;
import works.hop.presso.api.view.IViewEngine;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;

public class ViewEnginePlugin implements IViewEnginePlugin {

    private ServiceLoader<IViewEngine> loader;
    private String viewEngine;

    public ViewEnginePlugin(ServiceLoader<IViewEngine> loader) {
        this.loader = loader;
    }

    @Override
    public ServiceLoader<IViewEngine> component() {
        return this.loader;
    }

    @Override
    public void component(ServiceLoader<IViewEngine> loader) {
        this.loader = loader;
    }

    @Override
    public void name(String name) {
        this.viewEngine = name;
    }

    @Override
    public String name() {
        return this.viewEngine;
    }

    @Override
    public void templateDir(String dirName) {
        find(viewEngine).templateDir(dirName);
    }

    @Override
    public String templateDir() {
        return find(viewEngine).templateDir();
    }

    @Override
    public String mergeTemplate(String fileName, Map<String, Object> model) throws IOException {
        return find(viewEngine).mergeTemplate(fileName, model);
    }

    @Override
    public String mergeContent(String content, Map<String, Object> model) throws IOException {
        return find(viewEngine).mergeContent(content, model);
    }

    @Override
    public String render(AppSettings settings, String template, Map<String, Object> model) throws IOException {
        return find(viewEngine).render(settings, template, model);
    }
}
