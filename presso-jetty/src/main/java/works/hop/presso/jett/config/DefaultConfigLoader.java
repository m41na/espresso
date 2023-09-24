package works.hop.presso.jett.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.function.BiConsumer;

public class DefaultConfigLoader {

    private DefaultConfigLoader() {
        //hide constructor
    }

    public static void load(String yamlFile, BiConsumer<Exception, DefaultConfig> callback) {
        try {
            LoaderOptions options = new LoaderOptions();
            options.setAllowDuplicateKeys(false);

            Yaml yaml = new Yaml(new Constructor(DefaultConfig.class, options));
            InputStream inputStream = DefaultConfigLoader.class
                    .getClassLoader()
                    .getResourceAsStream(yamlFile);
            DefaultConfig defaultConfig = yaml.load(inputStream);

            callback.accept(null, defaultConfig);
        } catch (Exception e) {
            callback.accept(e, null);
        }
    }
}
