## Espresso

> A container for static functions. It cannot be instantiated, and so it cannot hold instance variables.

##### IApplication express()

static function which returns an instance of IApplication (which also extends IRouter) that can be used as an entry
point when starting the server - You can add middleware and HTTP method routes (such as get, put, post, and so on).

##### IBodyParser json()

static function which handles application/json content - Returns middleware that only parses JSON and only looks at
requests where the Content-Type header matches the type option. It is registered with an application using it
__use(IBodyParser)__ function

##### IBodyParser raw()

static function which handles raw bytes, application/octet-stream content - Returns middleware that parses all bodies
as a byte[] array and only looks at requests where the Content-Type header matches the type option. It is registered
with an application using it __use(IBodyParser)__ function

##### IBodyParser text()

static function which handles text/plain content - Returns middleware that parses all bodies as a string and only looks
at requests where the Content-Type header matches the type option. It is registered with an application using it
__use(IBodyParser)__ function

##### IBodyParser urlEncoded()

static function which handles x-www-form-urlencoded encoded content - Returns middleware that only parses urlencoded
bodies and only looks at requests where the Content-Type header matches the type option. It is registered with an
application using it __use(IBodyParser)__ function

##### IBodyParser multipart(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)

static function which handles multipart/form-data encoded content - Returns middleware that only parses multipart bodies
and only looks at requests where the Content-Type header matches the type option. It is registered with an application
using it __use(IBodyParser)__ function

##### ResourceHandler staticFiles(IStaticOptions options)

static function configures a static resources loader without a context path

##### ContextHandler staticFiles(String context, IStaticOptions options)

static function configures a static resources loader for a given a context path

##### void startServer(String host, int port, Consumer<String> callback, IApplication entryApp)

static function starts the server using a given entry IApplication instance