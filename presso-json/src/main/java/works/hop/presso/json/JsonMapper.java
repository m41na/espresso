package works.hop.presso.json;

import java.io.IOException;
import java.util.Properties;

public class JsonMapper {

    private final Properties configuration = new Properties();

    public JsonMapper(String configFile) {
        if (configFile != null) {
            try {
                this.configuration.load(JNodeParser.class.getClassLoader().getResourceAsStream(configFile));
            } catch (IOException e) {
                System.out.println("No json config properties found");
            }
        }
    }

    public <T> T parse(String json, Class<T> type) {
        //TODO: pick up from here - marshall json into its object representation
        return null;
    }

    public String apply(Object object) {
        //TODO: pick up from here - marshall object into its json representation
        return null;
    }
}
