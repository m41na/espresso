# Espresso web framework

This is a java-based, web framework that stays as much as possible close to the APIs in Express js in terms of the core abstractions and the corresponding functions and having only accomodating a few differences where the nuances of using Java could not sufficiently accomodate the succinctness or brevity of Javascript.

These core abstractions are:

|  Express js   |   Espresso jetty
|---------------|------------------
|  express()    |  Espresso
|  Application  |  IApplication
|  Request      |  IResponse
|  Response     |  IResponse
|  Router       |  IRouter

## Expresso

> A container for static functions. It cannot be instantiated, and so it cannnot hold instance values.

### IApplication express()

static function which returns an instance of IApplication (it also extends IRouter) which can be used as an entry point when starting the server - You can add middleware and HTTP method routes (such as get, put, post, and so on).

### IBodyParser json()

static function which handles application/json content - Returns middleware that only parses JSON and only looks at requests where the Content-Type header matches the type option. It is registered with an application using it __use(IbodyParser)__ function

### IBodyParser raw()

static function which handles raw bytes, application/octet-stream content - Returns middleware that parses all bodies as a byte[] array and only looks at requests where the Content-Type header matches the type option. It is registered with an application using it __use(IbodyParser)__ function

### IBodyParser text()

static function which handles text/plain content - Returns middleware that parses all bodies as a string and only looks at requests where the Content-Type header matches the type option. It is registered with an application using it __use(IbodyParser)__ function

### IBodyParser urlEncoded()

static function which handles x-www-form-urlencoded encoded content - Returns middleware that only parses urlencoded bodies and only looks at requests where the Content-Type header matches the type option. It is registered with an application using it __use(IbodyParser)__ function

### IBodyParser multipart(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)

static function which handles multipart/form-data encoded content - Returns middleware that only parses multipart bodies and only looks at requests where the Content-Type header matches the type option. It is registered with an application using it __use(IbodyParser)__ function

### ResourceHandler staticFiles(IStaticOptions options)

static function configures a static resources loader without a context path

### ContextHandler staticFiles(String context, IStaticOptions options)

static function configures a static resources loader for a given a context path

### void startServer(String host, int port, Consumer<String> callback, IApplication entryApp)

static function starts the server using a given entry IApplication instance

## IApplication

> The app object conventionally denotes the Espresso application. Create it by calling the top-level express() static function available in the Espresso class:

```
public static void main(String[] args) {
    var app = express();

    app.get("/", (req, res, next) -> res.send("hello world example"));
    app.locals().put("title", "My App");

    app.listen(3000);
}
```

The app object has methods for

1. Routing HTTP requests; see for example, app.METHOD and app.param.
2. Configuring middleware; see app.route.
3. Rendering HTML views; see app.render.
4. Registering a template engine; see app.engine.
5. It also has settings (properties) that affect how the application behave


