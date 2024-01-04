# Jipress web framework

This is a Java-based, web framework which, as much as practically possible, stays close to the interfaces exposed in Express.js in terms of
core abstractions and the corresponding functions and having to only accommodate a few differences where the nuances
of using Java can not sufficiently accommodate the succinctness or brevity of Javascript.

These core Express abstractions and their corresponding equivalent in Espresso are:

| Express.js  | Jipress jetty   |
|-------------|-----------------|
| express()   | Espresso        |
| Application | IApplication    |
| Request     | IRequest        |
| Response    | IResponse       |
| Router      | IRouter         | 

Although an instance of Espresso can be started using default configuration values alone, it may sometimes become
necessary to override the defaults using custom values. This can be accomplished through the use of separate module, 
namely _presso-cli_.

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
11. __routerHandlers__ - subdirectory for router handlers plugin jars, which is relative to the home directory

## IPlugin<T>

The parent interface through which additional implementations or overriding existing implementations can be provided. 
Currently, the following are the existing sub-interfaces:

1. __IViewEnginePlugin extends IViewEngine, IPlugin<IViewEngine>__ - View Engine
2. __IBodyParserPlugin extends IBodyParser, IPlugin<IBodyParser>__ - Body Parser
3. __IRouterHandlePlugin extends IRouterHandle, IPlugin<IRouterHandle>__ - Route Handler

### void id(String value)

Used to set a unique identifier for the class implementing the interface. This is necessary so that the _ServiceLoader_
will find and enumerate the plugin

### String id()

Used to return the unique identifier for a plugin which can be used, for instance, to identify a plugin when 
enumerating through a collection of plugins

### ServiceLoader<T> loader()

Returns the _ServiceLoader_ instance which contains an iterator for plugins that it has detected and loaded

### void loader(ServiceLoader<T> loader)

Sets in the plugin a _ServiceLoader_ which will be used to load and find plugins

### T find(String identifier)

Used to retrieve a plugin from the _ServiceLoader_ while enumerating through its collection of plugins

### void load(Class<T> type)

Delegate the load request to the underlying _ServiceLoader_ instance

### void load(Class<T> type, ClassLoader parentCl)

Delegate the load request to the underlying _ServiceLoader_ instance, and explicitly specifying the _ClassLoader_ 
instance to be used for loading the java classes

### void reload()

Delegate the reload request to the underlying _ServiceLoader_ instance

