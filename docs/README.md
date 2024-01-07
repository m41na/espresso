# Jipress

This is a Java-based, web framework which, as much as practically possible, stays close to the interfaces exposed in
Express.js in terms of
core abstractions and the corresponding functions and having to only accommodate a few differences where the nuances
of using Java can not sufficiently accommodate the succinctness or brevity of Javascript.

The core Express abstractions and their corresponding Espresso equivalent are:

| Express.js  | Jipress jetty |
|-------------|---------------|
| express()   | Espresso      |
| Application | IApplication  |
| Request     | IRequest      |
| Response    | IResponse     |
| Router      | IRouter       | 

Although an instance of Espresso can be started using default configuration values alone, it may sometimes become
necessary to override the defaults using custom values. This can be accomplished through the use of a separate module,
namely _presso-cli_.

## Basic Examples

The library can be used in three distinct ways:

1. Extend and use as an embedded web server
2. Creating plugins and adding them to a designated plugins folder for the web server application to load
3. Creating extensions and adding them to a designated extensions folder for the web server application to load

A _plugin_ differs from an _extension_ in several significant ways:

1. Plugins implement the _IPlugin_ interface while Extensions implement the _IExtension_
2. In a _Plugin_, the implementing class is provided with a _IRouter_ where it can then attach instances of
   _Middleware_, and then the application will add this _IRouter_ to itself as a child application.
3. In an _Extension_, the implementing class is provided with an _extensionPoint_ where it can then attach onto
   additional behavior - ```<T> void extendWith(T extensionPoint)``. Specifically, the extension point is the
   _ContextHandlerCollection_ and the additional behavior is in the form of a new ContextHandler.
4. The level of familiarity with the underlying infrastructure necessary to implement either of the two options is more
   low-level when working with _IExtension_than when working with _IPlugin_

The examples below are for the first use-case - using the library as an embedded http server in an application.
The other use-cases require implementing different interfaces and adding jars to designated folders.

```bash
void main() {
     var app = Espresso.express();
     app.get("/", (req, res, next) -> {
       res.send("Hello world");
     });
     
     app.listen(3000);
}
```

The _next_ parameter allows for _multiple handlers_ to be chained together with the main handler. _next_ can either be
_ok_ or indicate an _error_. Only one of the handlers should terminate the request

```bash
var app = Espresso.express();

IMiddleware cb0 = (req, res, next) -> {
   System.out.println("CB0");
   next.ok();
};

IMiddleware cb1 = (req, res, next) -> {
   System.out.println("CB1");
   next.error(new RuntimeException("cb1 is experiencing turbulence"));
   res.send("Interrupted from CB1!");
};

IMiddleware cb2 = (req, res, next) -> {
   System.out.println("CB2");
   res.send("Hello from CB2!");
};

// curl http://localhost:3000/example/b
app.get("/example/b", cb0, cb2);

// curl http://localhost:3000/example/a
app.get("/example/a", cb0, cb1, cb2);

app.listen(3000);
```

Serving _Static files_ can be accomplished using a builder, and can optionally use a context path

```bash
var app = Espresso.express();

app.use("/static", IStaticOptionsBuilder.newBuilder()
       .baseDirectory("presso-demos/www")
       .welcomeFiles("method-handlers.html")
       .acceptRanges(true)
       .listDirectories(false).build());
 app.all("/es", (req, res, next) -> res.send("ALL Holla Mundo!"));

// curl http://localhost:3000/en
app.method("get", "/en", (req, res, next) -> res.send("GET Hello World!"));

app.listen(3000);
```

The handlers can work with regular expressions in the path

```bash
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
```

_Routers_ can be nested independently into a parent _IApplication_ and the correct handler will be reached through a
recursive lookup based on the request path. This is exactly how _RouterHandleCallback_ plugin callback mechanism works.

```bash
var app = Espresso.express();

var route = app.route("/book");
// curl http://localhost:3000/book/
route.get("/", (req, res, next) -> res.send("Get any book"));
// curl -X POST http://localhost:3000/book/
route.post("/", (req, res, next) -> res.send("Add a book"));
// curl -X PUT http://localhost:3000/book/
route.put("/", (req, res, next) -> res.send("Update the book"));
// curl -X DELETE http://localhost:3000/book/
route.delete("/", (req, res, next) -> res.send("Remove the book"));
app.engine(IViewEngine.MVEL, "presso-demos/templates", ".mvel");

// curl http://localhost:3000/
app.get("/", (req, res, next) ->
       res.render("template", Map.of("name", "Mia")));

app.listen(3000);
```

Currently, only two view engines are supported out-of-the-box - _MVEL_ and _PEBBLE_. It is easy to provide additional
view engine implementations using plugins. To make use of a view engine, the following setup would be sufficient.

```bash
var app = Espresso.express();

app.set(AppSettings.Setting.VIEW_ENGINE.property, "mvel");
app.set(AppSettings.Setting.TEMPLATES_EXT.property, ".mvel");
app.set(AppSettings.Setting.TEMPLATES_DIR.property, "presso-demos/templates");

// curl -X GET http://localhost:3000"
app.get("/", (req, res, next) -> {
   res.render("lookingGood", Map.of("name", "this"));
});

app.listen(3000);
```

Similar to view engines, additional request body parsers can be implemented using plugins, and even the existing
ones can be overridden if there is a need for it. This is how the existing body parsers can be leveraged.

```bash
var app = Espresso.express();

app.use(IStaticOptionsBuilder.newBuilder().baseDirectory("presso-demos/www").welcomeFiles("content-handlers.html").build());
app.use(Espresso.multipart(Paths.get(System.getProperty("java.io.tmpdir"), "upload").toString()));
app.use(Espresso.urlEncoded());
app.use(Espresso.json());
app.use(Espresso.raw());

// curl -X POST http://localhost:3000/json -H "Content-Type: application/json"
// -d '{"name": "Jimmy", "age": 20}'
app.post("/json", (req, res, next) -> {
   Object body = req.body();
   ReqCookies cookies = req.cookies();
   res.json(List.of(body, cookies));
});

// curl -X POST http://localhost:3000/multipart -H "Content-Type: multipart/form-data"
// -F name=sample-file
// -F content=@/c/Projects/java/espresso/presso-demos/demo/multipart.txt
app.post("/multipart", (req, res, next) -> {
   Object result = req.body();
   log.info("multipart upload - {}", result);
   res.sendStatus(201);
});

// curl -X POST http://localhost:3000/formencoded -H "Content-Type: application/x-www-form-urlencoded"
// -d "param1=value1&param2=value2"
app.post("/formencoded", (req, res, next) -> {
   Object form = req.body();
   res.send(form.toString());
});

// curl -X POST http://localhost:3000/download -H "Content-Type: application/x-www-form-urlencoded"
// -d "folder=/c/Projects/java/espresso/presso-demos/demo&fileName=download.txt"
app.post("/download", (req, res, next) -> {
   Map<String, Object> options = req.body();
   res.download(
           ((ArrayList<?>) options.get("folder")).get(0).toString(),
           ((ArrayList<?>) options.get("fileName")).get(0).toString(),
           null,
           error -> {
               if (error == null) {
                   res.end();
               } else {
                   res.status(500);
                   res.send(error.getMessage());
               }
           });

});

app.listen(3000);
```

Although error handling is handled using a default error handler, it may sometime be necessary to tailor how the
error is interpreted and transmitted back to the client. In such cases, the _IErrorHandler_ interface ia available for
use.

```bash
var app = Espresso.express();
app.use(Espresso.text());
// registering a custom IErrorHandler 
app.use((error, req, res, next) -> {
   if (next.errorCode().equals("1001")) {
       res.end("Custom Error Code 1001 handled", Charset.defaultCharset().name());
       next.setHandled(true);
   }
});

// curl localhost:3000 -> All clear
// curl localhost:3000?err=throw -> Custom Error Code 1001 handled
app.get("/", (req, res, next) -> {
   List<String> err = req.query("err", String::toString);
   if (err.contains("throw")) {
       // throw exception having an error code
       next.error("1001", new RuntimeException("Get me out of here"));
   } else {
       res.send("All clear");
   }
});

app.listen(3000);
```

To make use of websocket, it's also a simple set-up as demonstrated below.

```bash
var app = Espresso.express();

app.use("/", IStaticOptionsBuilder.newBuilder()
       .baseDirectory("presso-demos/www")
       .welcomeFiles("web-socket.html")
       .acceptRanges(true)
       .listDirectories(false)
       .build());
       
app.websocket("/ws/", WebsocketOptionsBuilder.newBuilder()
       .subProtocols(List.of("protocolOne"))
       .pulseInterval(20000)
       .websocketPath("/events/*")
       .build(), (ws) -> {

   ws.onConnect(session -> {
       Session sess = (Session) session;
       System.out.println("Socket connected: " + session);
       try {
           sess.getRemote().sendString("Connection accepted");
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   });

   ws.onClose((status, reason) -> System.out.printf("Socket closing: %d, %s\n", status, reason));

   ws.onMessage(message -> {
       System.out.println("Received TXT message: " + message);
       if (message.toLowerCase(Locale.ENGLISH).contains("bye")) {
           ((Session) ws.getSession()).close();
       }
   });

   ws.onBinary((bytes, offset, length) -> {
       System.out.println("Received BYTES message: " + new String(bytes, offset, length));
   });

   ws.onError(cause -> cause.printStackTrace(System.err));
});

app.listen(8888);
```

For completeness with the _websocket_ example, here are the client-side files to play with

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Web Socket Home</title>
    <link href="css/ws.css" rel="stylesheet"/>
    <script defer src="js/ws.js"></script>
</head>
<body>
<form id="chat-form">
    <label autofocus for="message" tabindex="1"> Type your message here
        <textarea cols="100" id="message" rows="5"></textarea>
    </label>
    <input onclick="sendText()" tabindex="2" type="button" value="Send"/>
</form>
<div id="chat-messages"></div>
</body>
</html>
```

And for javascript, here is some sauce to play with.

```js
const url = "ws://localhost:8888/ws/events"
let webSocket = new WebSocket(url, "protocolOne");
let clientID = "";

webSocket.onopen = (event) => {
    webSocket.send("Here's some text that the server is urgently awaiting!");
};

function setUsername() {
    console.log("currently doing nothing")
}

webSocket.onmessage = (event) => {
    const content = document.getElementById("chat-messages").contentDocument;
    let text = "";
    const msg = JSON.parse(event.data);
    const time = new Date(msg.date);
    const timeStr = time.toLocaleTimeString();

    switch (msg.type) {
        case "id":
            clientID = msg.id;
            setUsername();
            break;
        case "username":
            text = `User <em>${msg.name}</em> signed in at ${timeStr}<br>`;
            break;
        case "message":
            text = `(${timeStr}) ${msg.name} : ${msg.text} <br>`;
            break;
        case "rejectusername":
            text = `Your username has been set to <em>${msg.name}</em> because the name you chose is in use.<br>`;
            break;
        case "userlist":
            document.getElementById("userlistbox").innerHTML = msg.users.join("<br>");
            break;
    }

    if (text.length) {
        content.write(text);
        document.getElementById("chat-messages").contentWindow.scrollByPages(1);
    }
};

// Send text to all users through the server
function sendText() {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    const msg = {
        type: "message",
        text: document.getElementById("message").value,
        id: clientID,
        date: Date.now(),
    };

    // Send the msg object as a JSON-formatted string.
    webSocket.send(JSON.stringify(msg));

    // Blank the text input element, ready to receive the next line of text from the user.
    document.getElementById("message").value = "";
}

function closeConn() {
    webSocket.close();
}
```

> Plugin implementation

Each of the direct implementations for the _IPlugin_ interface implements the _find(String identifier)_ function,
which marks a clear point of separation why sub-interfaces diverge. For example, the _IRouterHandlePlugin_ will only
now deal with the type _IRouterHandle_ moving forward

```bash
public interface IRouterHandlePlugin extends IRouterHandle, IPlugin<IRouterHandle> {

    @Override
    default IRouterHandle find(String identifier) {
        for (IRouterHandle plugin : loader()) {
            if (plugin.contextPath().equals(identifier)) {
                return plugin;
            }
        }
        return null;
    }
}
```

The concrete class _RouterHandlePlugins_ the proceeds to provide the rest of the additional implementation details that
are missing
and that are specific to _IRouterHandle_ type. This pattern is repeated for the other two implementations -
_IBodyParserPlugin_ and
_IViewEnginePlugin_

```bash
public class RouterHandlePlugins implements IRouterHandlePlugin {

    private ServiceLoader<IRouterHandle> loader;
    private String contextPath;

    public RouterHandlePlugins(ServiceLoader<IRouterHandle> loader) {
        this.loader = loader;
    }

    @Override
    public void id(String value) {
        this.contextPath = value;
    }
    
    <content truncated>
}
```

The other half of the _plugins_ equation is provided through implementations of the _IPluginCallback_ interface -
_BodyParserCallback_, _ViewEnginesCallback_ and _RouterHandleCallback_. These provide hooks for the server application
to call and load the plugins during startup.

> Extension implementation

Implementing an _IExtension_ is similar in many ways to implementing a _IPlugin_, but at lower levels, they diverge.
In _IPlugin_, the key abstraction is attaching _Middleware_ instances to the provided _IRouter_ instance.
In _IExtension_, the key abstraction is to roll out one's own servlet context handler, and then attaching it to the
provided ContextHandlerCollection.

The example below illustrates attaching a _FastCgiServlet_ and handling a _WordPress_ application.

```bash
System.setProperty("WORDPRESS_HOME", "C:\\Apps\\wordpress");
Directories.PLUGINS.put(DirectoryInfo.PLUGINS_HOME, "<project dir>\\espresso");
Directories.PLUGINS.put(DirectoryInfo.CTX_EXTENSIONS, "ext-wordpress/build/libs");
var app = Espresso.express();
app.listen(9080);
```

The WordPress extension is shown below.

```java
public class WordPressExtension implements IExtension {

    String contextPath = "/";
    String resourceRoot = System.getProperty("WORDPRESS_HOME");

    // <setters/getters redacted for brevity>

    @Override
    public <T> void extendWith(T extensionPoint) {
        try {
            ServletContextHandler context = createServletContext(this.contextPath(), this.resourceRoot());
            configureTryFilesFilter(context);
            configureDefaultServletHandler(context);
            configureFastCgiHandler(context, this.resourceRoot());
            ((ContextHandlerCollection) extensionPoint).addHandler(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

> Configuring a WordPress application

Although this WordPress section is a detour from the rest of the documentation, it's nonetheless an exhilarating
adventure in itself just to be able to run a _PHP_ application using a _Java_ web server.

## Configuring Jetty for fastcgi

These links provide some additional reading material on how to configure Jetty for FastCgi

1. [Jetty 9 docs](https://eclipse.dev/jetty/documentation/jetty-9/index.html#fastcgi)
2. [Jetty-Users board](https://www.eclipse.org/lists/jetty-users/msg09434.html)

## Start php interpreter

To successfully run WordPress, _Jetty_ will require a _PHP_ interpreter which will handle the _PHP_ requests, and
then send back the response to _Jetty_, and finally back to the user. For this reason, it's important to configure
and run a _PHP_ interpreter process.

1. [Download php for Windows](https://windows.php.net/download/)
2. Unzip files in a location of your choice - let's assume ```c:\\tools\\php-8.3.1```
3. Add php path to environment variables
4. In a new terminal, start the php-cgi.exe process ```php-cgi.exe -b 127.0.0.1:9000```
5. Find additional [information here](https://www.nginx.com/resources/wiki/start/topics/examples/phpfastcgionwindows/)

The process started in step 4 is a listener that interprets php input and replies to the caller with the result.
In this case, the caller is Jetty

## Start mysql server instance

[Reference docs](https://dev.mysql.com/doc/mysql-linuxunix-excerpt/8.0/en/docker-mysql-getting-started.html)

1. Pull image - ```docker pull container-registry.oracle.com/mysql/community-server```
2. Start
   container - ```docker run --name=local-mysql-server --restart on-failure -p 3306:3306 -d container-registry.oracle.com/mysql/community-server```
3. Retrieve generated password - ```docker logs local-mysql-server 2>&1 | grep GENERATED```
4. Use generated password to sign into container - ```docker exec -it local-mysql-server mysql -uroot -p```
5. Change default password - ```mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '<new password>';```
6. Exec into container to look around - ```docker exec -it local-mysql-server bash```

While signed in as _root_, create a new non-root user for use with installing wordpress

1. create new user (let's assume the name mysqluser)
   ```mysql> CREATE USER 'mysqluser'@'%' IDENTIFIED BY '<user password>';```
2. grant super user permissions
   ```mysql> GRANT ALL PRIVILEGES on *.* to 'mysqluser'@'%';```
3. reload mysql configuration
   ```mysql> FLUSH PRIVILEGES;```

# Run a WordPress application

On Windows, some additional steps will be required before continuing.

In the php installation folder:

1. Copy and paste php.ini-production file, changing its name for php.ini (root directory of PHP)
2. Uncomment the following lines

   ;extension=mysqli
   ;extension_dir=ext

3. [Download WordPress](https://wordpress.org/download/)
4. Follow
   the [installation guide](https://developer.wordpress.org/advanced-administration/before-install/howto-install/)

## Startup Option

These are defined and documented in the __OptBuilder__ class

1. __host__ - hostname or IP of computer running applications
2. __port__ - tcp port where application is listening for client requests
3. __securePort__ - ssl tcp port where application is listening for client requests
4. __keystorePath__ - file system path to server truststore
5. __keystorePass__ - password for server truststore
6. __redirectSecure__ - require the server to redirect all requests from the insecure _http_ to the secure protocol
   _https_
7. __deployEnv__ - equivalent to _NODE_ENV_ in Node.js. It's a variable which is used to indicate the expected
   behavior of an application based on the environment in which it is deployed
8. __pluginsHome__ - home directory for plugin jars
9. __viewEngines__ - subdirectory for view engine plugin jars, which is relative to the home directory
10. __bodyParsers__ - subdirectory for body parser plugin jars, which is relative to the home directory
11. __routerHandles__ - subdirectory for router handle plugin jars, which is relative to the home directory
12. __watch__ - watch plugins directory and reload whenever a change (addition/removal) is detected
13. __resourcesCtx__ - no context will use ResourceHandler while having a context will use DefaultServlet
14. __baseDirectory__ - root directory for resolving requests for static resources
15. __welcomeFiles__ - list of comma-separated file names that will be displayed by default when the resources directory
    is reached
16. __acceptRanges__ - accepts http server code 206. Range support in static content is supported using DefaultServlet
    and not in ResourceHandler.
17. __listDirectories__ - show files and directories in the parent folder when a folder matches the request url

## IPlugin<T>

The base interface from which additional implementations can be provided. Currently, the following are the existing,
direct sub-interfaces:

1. __IViewEnginePlugin extends IViewEngine, IPlugin<IViewEngine>__ - View Engine
2. __IBodyParserPlugin extends IBodyParser, IPlugin<IBodyParser>__ - Body Parser
3. __IRouterHandlePlugin extends IRouterHandle, IPlugin<IRouterHandle>__ - Route Handler

#### void id(String value)

Used to set a unique identifier for the class implementing the interface. This is necessary so that the _ServiceLoader_
will find and enumerate the plugin

#### String id()

Used to return the unique identifier for a plugin which can be used, for instance, to identify a plugin when
enumerating through a collection of plugins

#### ServiceLoader<T> loader()

Returns the _ServiceLoader_ instance which contains an iterator for plugins that it has detected and loaded

#### void loader(ServiceLoader<T> loader)

Sets in the plugin a _ServiceLoader_ which will be used to load and find plugins

#### T find(String identifier)

Used to retrieve a plugin from the _ServiceLoader_ while enumerating through its collection of plugins

#### void load(Class<T> type)

Delegate the load request to the underlying _ServiceLoader_ instance

#### void load(Class<T> type, ClassLoader parentCl)

Delegate the load request to the underlying _ServiceLoader_ instance, and explicitly specifying the _ClassLoader_
instance to be used for loading the java classes

#### void reload()

Delegate the reload request to the underlying _ServiceLoader_ instance

## IPluginCallback

This interface provides the hooks for loading the plugins during application startup

#### void onEvent(String event, IApplication app)

Delegates event handling to either _load_ or _reload_ depending on the event received.

#### void reloadPlugins(IApplication app)

Finds the appropriate implementation for _IPlugin_ and calls the necessary methods to _load_ plugins and any other
additional initialization wherever it may be necessary

#### void loadPlugins(IApplication app)

Finds the appropriate implementation for _IPlugin_ and calls the necessary methods to _reload_ plugins and any other
additional cleanup or initialization wherever it may be necessary
