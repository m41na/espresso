package works.hop.presso.api.view;

import works.hop.presso.api.application.AppSettings;

import java.io.IOException;
import java.util.Map;

public interface IViewEngine {

    String MVEL = "mvel";
    String PEBBLE = "pebble";

    String name();

    String mergeTemplate(String fileName, Map<String, Object> model) throws IOException;

    String mergeContent(String content, Map<String, Object> model) throws IOException;

    String render(AppSettings settings, String template, Map<String, Object> model) throws IOException;
}
