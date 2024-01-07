package com.akilisha.espresso.demos;

import lombok.extern.slf4j.Slf4j;
import com.akilisha.espresso.jett.Espresso;

import java.nio.charset.Charset;

@Slf4j
public class RegexPathHandlers {

    public static void main(String[] args) {
        var app = Espresso.express();

        // curl http://localhost:3000/random.text
        app.get("/random.text", (req, res, next) -> {
            res.send("random.text");
        });

        // curl http://localhost:3000/bcd
        // curl http://localhost:3000/abcd
        app.get("/a?bcd", (req, res, next) -> {
            res.send("a?bcd");
        });

        // curl http://localhost:3000/abccd
        app.get("/abc+d", (req, res, next) -> {
            res.send("abc+d");
        });

        //  curl http://localhost:3000/abbbbcd
        app.get("/ab*cd", (req, res, next) -> {
            res.send("ab*cd");
        });

        // curl http://localhost:3000/abe
        app.get("/ab(cd)?e", (req, res, next) -> {
            res.send("ab(cd)?e");
        });

        // curl http://localhost:3000/a/
        app.get("/a/", (req, res, next) -> {
            res.send("/a/");
        });

        // curl http://localhost:3000/housefly
        // curl http://localhost:3000/butter-fly
        app.get("/.*fly$", (req, res, next) -> {
            res.send("/.*fly$");
        });

        // curl http://localhost:3000/users/1/books/20
        app.get("/users/:userId/books/:bookId", (req, res, next) -> {
            res.end(req.params(), Charset.defaultCharset().name());
        });

        // curl http://localhost:3000/flights/nyc-lax
        app.get("/flights/:from-:to", (req, res, next) -> {
            res.end(req.params(), Charset.defaultCharset().name());
        });

        // curl http://localhost:3000/plantae/homo.sapien
        app.get("/plantae/:genus.:species", (req, res, next) -> {
            res.end(req.params(), Charset.defaultCharset().name());
        });

        // curl http://localhost:3000/user/1
        app.get("/user/:userId", (req, res, next) -> {
            res.end(req.params(), Charset.defaultCharset().name());
        });

        app.listen(3000);
    }
}
