package com.akilisha.espresso.demos;

import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.plugin.Directories;
import com.akilisha.espresso.api.plugin.DirectoryInfo;
import com.akilisha.espresso.jett.Espresso;

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
