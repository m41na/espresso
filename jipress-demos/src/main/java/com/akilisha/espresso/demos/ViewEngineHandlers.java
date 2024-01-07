package com.akilisha.espresso.demos;

import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.api.application.AppSettings;
import com.akilisha.espresso.jett.Espresso;

import java.util.Map;

@Slf4j
public class ViewEngineHandlers {

    public static void main(String[] args) {
        var app = Espresso.express();

        app.set(AppSettings.Setting.VIEW_ENGINE.property, "mvel");
        app.set(AppSettings.Setting.TEMPLATES_EXT.property, ".mvel");
        app.set(AppSettings.Setting.TEMPLATES_DIR.property, "jipress-demos/templates");

        // curl -X GET http://localhost:3000"
        app.get("/", (req, res, next) -> {
            res.render("lookingGood", Map.of("name", "this"));
        });

        app.listen(3000);
    }
}
