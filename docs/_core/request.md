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

### IApplication app()

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

### String baseUrl()

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

### Map<String, Object> body()

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

### <T> T body(Function<byte[], T> converter)

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

### void upload()

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

### ReqCookies cookies()

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

### String hostname()

Contains the hostname derived from the Host HTTP header.

```bash
// Host: "example.com:3000"
console.dir(req.hostname())
// => 'example.com'
```

### String ip()

Contains the remote IP address of the request.

```bash
console.dir(req.ip())
// => '127.0.0.1'
```

### String[] ips()

Contains the remote IP address of the request as an array.

```bash
console.dir(req.ip())
// => ['127.0.0.1']
```

### String method()

Contains a string corresponding to the HTTP method of the request: GET, POST, PUT, and so on.

### String originalUrl()

Contains the entire request url which is received from the client

```bash
// GET /search?q=something
console.dir(req.originalUrl)
// => '/search?q=something'
```

### String param(String name)

Returns the named path parameter value by drawing from the map of such path parameters that is created with every
request

### Map<String, String> params()

This property is an object containing properties mapped to the named route “parameters”. For example, if you have the
route /user/:name, then the “name” property is available as req.params.name. This object defaults to {}.

```bash
// GET /user/tj
console.dir(req.params('name')
// => 'tj'
```

### <T> T param(String name, Function<String, T> converter)

Similar to __param(name)__ byt with the addition of a converter function that will return a static type

### String path()

Contains the path part of the request URL. Similar to the _req.originalUrl()__

```bash
// example.com/users?sort=desc
console.dir(req.path)
// => '/users'
```

### String protocol()

