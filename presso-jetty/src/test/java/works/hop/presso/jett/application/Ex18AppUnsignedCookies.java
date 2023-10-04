package works.hop.presso.jett.application;

import works.hop.presso.api.cookie.CookieOptions;
import works.hop.presso.api.cookie.CookieOptionsBuilder;

import java.util.concurrent.TimeUnit;

import static works.hop.presso.jett.Espresso.express;

//TODO: Cookies require more testing to correctly anticipate expected behavior - both signed and unsigned
public class Ex18AppUnsignedCookies {

    public static void main(String[] args) {
        var app = express();

        app.get("/", (req, res, next) -> {
            CookieOptions cookieOptions = CookieOptionsBuilder.newBuilder().maxAge(30, TimeUnit.MINUTES).path("/cookie").build();
            res.cookie("monster", "frankenstein", cookieOptions);
            res.send("setting cookies example");
        });

        app.get("/cookie", (req, res, next) -> {
            req.cookies().forEach(map -> {
                map.forEach((key, value) -> System.out.println(key + ": " + value));
            });
            res.send("hello cookies example");
        });

        app.get("/clear", (req, res, next) -> {
            res.clearCookie("monster");
            res.send("clearing cookies example");
        });

        app.listen(3000);
    }
}
