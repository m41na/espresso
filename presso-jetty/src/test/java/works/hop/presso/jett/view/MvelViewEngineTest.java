package works.hop.presso.jett.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mvel2.templates.TemplateCompiler;
import works.hop.presso.api.application.AppSettings;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MvelViewEngineTest {

    Path templateDir = Path.of(System.getProperty("user.dir"), "mvel");
    MvelViewEngine viewEngine = new MvelViewEngine(templateDir.toString());

    AppSettings settings = new AppSettings();

    Map<String, Object> model = Map.of("name", "Jimmy", "age", 22, "title", "Volcanic Ash");

    @BeforeEach
    void setUp() {
        settings.put(AppSettings.Setting.TEMPLATES_EXT, ".mv");
        viewEngine.registry.addNamedTemplate("headerTemplate", TemplateCompiler.compileTemplate(
                Path.of(templateDir.toString(), "header.mv").toFile()));
    }

    @Test
    @Disabled
    void mergeTemplate() {
        String templateFile = "person.mv";
        String view = viewEngine.mergeTemplate(templateFile, model);
        assertThat(view).isNotEmpty();
        assertThat(view).contains("My name is Jimmy");
    }

    @Test
    void mergeContent() {
        String templateContent = "My name is @{name}";
        String view = viewEngine.mergeContent(templateContent, model);
        assertThat(view).isNotEmpty();
        assertThat(view).contains("My name is Jimmy");
    }

    @Test
    @Disabled
    void render() {
        String view = viewEngine.render(settings, "person", model);
        assertThat(view).isNotEmpty();
        assertThat(view).contains("My name is Jimmy");
    }
}