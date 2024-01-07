package works.hop.presso.demos;

import lombok.extern.slf4j.Slf4j;
import works.hop.presso.api.plugin.Directories;
import works.hop.presso.api.plugin.DirectoryInfo;
import works.hop.presso.jett.Espresso;

@Slf4j
public class WordPressExtension {

    public static void main(String[] args) {
        System.setProperty("WORDPRESS_HOME", "C:\\Projects\\wordpress");
        Directories.PLUGINS.put(DirectoryInfo.PLUGINS_HOME, "C:\\Projects\\java\\espresso");
        Directories.PLUGINS.put(DirectoryInfo.CTX_EXTENSIONS, "ext-wordpress/build/libs");
        var app = Espresso.express();
        app.listen(9080);
    }
}
