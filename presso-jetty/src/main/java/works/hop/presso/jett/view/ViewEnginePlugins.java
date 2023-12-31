package works.hop.presso.jett.view;

import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.plugin.IViewEnginePlugin;
import works.hop.presso.api.view.IViewEngine;

import java.io.IOException;
import java.util.Map;
import java.util.ServiceLoader;

public class ViewEnginePlugins implements IViewEnginePlugin {

    private ServiceLoader<IViewEngine> loader;
    private String viewEngine;

    public ViewEnginePlugins(ServiceLoader<IViewEngine> loader) {
        this.loader = loader;
    }

    @Override
    public ServiceLoader<IViewEngine> loader() {
        return this.loader;
    }

    @Override
    public void loader(ServiceLoader<IViewEngine> loader) {
        this.loader = loader;
    }

    @Override
    public void id(String value) {
        this.viewEngine = value;
    }

    @Override
    public String id() {
        return this.viewEngine;
    }

    @Override
    public String name() {
        return this.id();
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
