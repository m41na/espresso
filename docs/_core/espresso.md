## Espresso

> A container for static functions. It cannot be instantiated, and so it cannot hold instance variables.

### IApplication express()

static function which returns an instance of IApplication (which also extends IRouter) that can be used as an entry
point when starting the server - You can add middleware and HTTP method routes (such as get, put, post, and so on).

```bash
var app = express();
```

### IBodyParser json()

static function which returns a handler for application/json content - Returns middleware that only parses JSON and
handles requests where the Content-Type header matches the type option.
This handler is loadable in two ways

1. Registered with an application using it __use(IBodyParser)__ function
2. Loaded as a plugin through the __IBodyParserPlugin__ interface

### IBodyParser raw()

static function which returns a handler for raw bytes, application/octet-stream content - Returns middleware that
parses all bodies as a byte[] array and handles requests where the Content-Type header matches the type option.
This handler is loadable in two ways

1. Registered with an application using it __use(IBodyParser)__ function
2. Loaded as a plugin through the __IBodyParserPlugin__ interface

### IBodyParser text()

static function which returns a handler for text/plain content - Returns middleware that parses all bodies as a string
and handles requests where the Content-Type header matches the type option.
This handler is loadable in two ways

1. Registered with an application using it __use(IBodyParser)__ function
2. Loaded as a plugin through the __IBodyParserPlugin__ interface

### IBodyParser urlEncoded()

static function which returns a handler x-www-form-urlencoded encoded content - Returns middleware that only parses
urlencoded bodies handles requests where the Content-Type header matches the type option.
This handler is loadable in two ways

1. Registered with an application using it __use(IBodyParser)__ function
2. Loaded as a plugin through the __IBodyParserPlugin__ interface

### IBodyParser multipart(String location)

static function which delegates to __multipart(location, maxFileSize, maxRequestSize, fileSizeThreshold)__ while
applying the following defaults that are defined in the __StartupEnv__ class as follows:

```bash
MULTIPART_LOCATION("location", "upload folder for multipart requests. Default is java's temp directory", System.getProperty("java.io.tmpdir")),
MULTIPART_MAX_FILE_SIZE("maxFileSize", "maximum size permitted for uploaded file. Default is 1MB", 1_000_000L),
MULTIPART_MAX_REQ_SIZE("maxRequestSize", "maximum size allowed for multipart/form-data request. Default is 10MB", 10_000_000L),
MULTIPART_FILE_THRESHOLD("fileSizeThreshold", "file size threshold before which it is written to disk. Default is 0", 0)
```

### IBodyParser multipart(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)

static function which returns a handler multipart/form-data encoded content - Returns middleware that only parses
multipart bodies and handles requests where the Content-Type header matches the type option.
This handler is loadable in two ways

1. Registered with an application using it __use(IBodyParser)__ function
2. Loaded as a plugin through the __IBodyParserPlugin__ interface

### ResourceHandler staticFiles(IStaticOptions options)

static function configures a static resources loader without a context path

### ContextHandler staticFiles(String context, IStaticOptions options)

static function configures a static resources loader for a given context path

### Server create(String host, int port, IApplication entryApp)

static function which creates a Server instance that has been configured with a _ThreadPool_, _ServerConnectors_, and
a _shutdown hook_ to terminate the _ExecutorService_.

### void startServer(String host, int port, Consumer<String> callback, IApplication entryApp)

static function starts the server using a given entry IApplication instance