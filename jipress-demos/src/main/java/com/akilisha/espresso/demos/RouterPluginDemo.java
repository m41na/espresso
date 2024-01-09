package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.servable.IStaticOptionsBuilder;
import com.akilisha.espresso.jett.Espresso;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

@Slf4j
public class RouterPluginDemo {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("jipress-demos/www").welcomeFiles("content-handlers.html").build());
        app.use(Espresso.multipart(Paths.get(System.getProperty("java.io.tmpdir"), "upload").toString()));
        app.listen(3000);
    }
}
