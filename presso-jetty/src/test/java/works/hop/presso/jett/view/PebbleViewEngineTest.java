package works.hop.presso.jett.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import works.hop.presso.api.application.AppSettings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PebbleViewEngineTest {

    PebbleViewEngine viewEngine = new PebbleViewEngine(
            Path.of(System.getProperty("user.dir"),
                    "pebble").toString());

    AppSettings settings = new AppSettings();

    @BeforeEach
    void setUp() {
        settings.put(AppSettings.Setting.TEMPLATES_EXT, ".html");
    }

    @Test
    void mergeTemplate() throws IOException {
        String templateFile = "home.html";
        String view = viewEngine.mergeTemplate(templateFile, Map.of("name", "Jimmy"));
        assertThat(view).isNotEmpty();
        assertThat(view).contains("My name is Jimmy");
    }

    @Test
    void mergeContent() throws IOException {
        String templateContent = "My name is {{ name }}";
        String view = viewEngine.mergeContent(templateContent, Map.of("name", "Jimmy"));
        assertThat(view).isNotEmpty();
        assertThat(view).contains("My name is Jimmy");
    }

    @Test
    void render() throws IOException {
        String view = viewEngine.render(settings, "home", Map.of("name", "Jimmy"));
        assertThat(view).isNotEmpty();
        assertThat(view).contains("My name is Jimmy");
    }
}