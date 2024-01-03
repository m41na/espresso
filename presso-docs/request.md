## Request

The req object represents the HTTP request and has properties for the request query string, parameters, body,
HTTP headers, and so on. In this documentation and by convention, the object is always referred to as req (and the HTTP
response is res) but its actual name is determined by the parameters to the callback function in which you’re working.

For example:

```bash
app.get("/user/:id", (req, res) {
  res.send("user " + req.params.id)
})
```

But you could just as well have:

```bash
app.get("/user/:id", function (request, response) {
  response.send("user " + request.params.id)
})
```

#### IApplication app()

This function returns a reference to the instance of the Express application that is using the middleware.

```bash
static IMiddleware middleware = (req, res, next) -> {
    res.send("The views directory is " + req.app().get("templates dir"));
    //expect - The views directory is my-view-folder
};

public static void main(String[] args) {
    var app = express();

    app.set(AppSettings.Setting.TEMPLATES_DIR.property, "my-view-folder");
    app.get("/", middleware);

    app.listen(3000);
}
```

#### String baseUrl()

The context path on which a application instance was mounted.

```bash
public static void main(String[] args) {
    var app = express();

    var greet = app.route("/greet");

    greet.get("/jp", (req, res, next) -> {
        System.out.println(req.baseUrl()); // /greet
        res.send("Konichiwa!");
    });
    
    var greet2 = express();

    greet2.get("/swa", (req, res, next) -> {
        System.out.println(req.baseUrl()); // /greet2
        res.send("Shikamoo!");
    });

    app.use("/greet2", greet2);

    app.listen(3000);
}
```

#### Map<String, Object> body()

Contains key-value pairs of data submitted in the request body. By default, it is undefined, and is populated when you
use body-parsing middleware such as express.json() or express.urlencoded().

```bash
public static void main(String[] args) {
    var app = Espresso.express();
    app.use(json());
    app.use(urlEncoded());

    app.get("/json", (req, res, next) -> {
        Map<String, Object> json = Map.of("name", "Janie", "age", 23);
        System.out.println(json);
        res.json(json);
    });

    app.get("/json", (req, res, next) -> {
        Map<String, Object> json = Map.of("name", "Janie", "age", 23);
        System.out.println(json);
        res.json(json);
    });

    app.post("/json", (req, res, next) -> {
        Map<String, Object> json = req.body();
        System.out.println(json);
        res.json(json);
    });

    app.post("/form", (req, res, next) -> {
        Map<String, Object> json = req.body();
        System.out.println(json);
        res.json(json);
    });

    app.listen(3000);
}
```

#### <T> T body(Function<byte[], T> converter)

Turns over request bytes to custom converter for user-defined conversion to desired type

> curl -X POST http://localhost:3000/string/ -H "Content-Type: text/plain" -d "Some cool stuff"

```bash
public static void main(String[] args) {
    var app = express();
    app.use(new PlainTextBodyParser());
    
    var greet = app.route("/");
    
    greet.post("/string", (req, res, next) -> {
        String plainText = req.body(String::new);
        res.send(plainText); // expect: Some cool stuff
    });
    
    app.listen(3000);
}
```

#### void upload()

Upload content to a designated directory configured through application properties

```bash
public static void main(String[] args) {
    var app = express();
    app.properties("app-default.yaml"); // load application configuration properties made available in Config Map
    app.use("/upload", StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").welcomeFiles("upload.html").build());
    app.use(multipart(
            System.getProperty("java.io.tmpdir"),
            ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getMaxFileSize(),
            ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getMaxRequestSize(),
            ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getFileSizeThreshold()));

    app.get("/", (req, res, next) -> {
        res.send("Hello, trying to upload");
    });

    app.post("/some", (req, res, next) -> {
        req.upload(); //this should upload content
        res.sendStatus(201);
    });

    app.listen(3000);
}
```

#### ReqCookies cookies()

When using cookie-parser middleware, this property is an object that contains cookies sent by the request. If the
request contains no cookies, it defaults to {}.

```bash
public static void main(String[] args) {
    var app = express();

    app.get("/", (req, res, next) -> {
        CookieOptions cookieOptions = CookieOptionsBuilder.newBuilder().build();
        res.cookie("monster", "frankenstein", cookieOptions);
        res.send("hello cookies example");
    });
    
    app.get("/cookie", (req, res, next) -> {
        res.
        res.send("hello cookies example");
    });

    app.listen(3000);
}
```