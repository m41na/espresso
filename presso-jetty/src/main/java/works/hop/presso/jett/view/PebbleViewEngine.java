package works.hop.presso.jett.view;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import lombok.RequiredArgsConstructor;
import works.hop.presso.api.application.AppSettings;
import works.hop.presso.api.view.IViewEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Map;

@RequiredArgsConstructor
public class PebbleViewEngine implements IViewEngine {

    private final String templateDir;
    private final PebbleEngine engine = new PebbleEngine.Builder().build();

    @Override
    public String name() {
        return PEBBLE;
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
