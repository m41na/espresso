# Espresso web framework

This is a Java-based, web framework which, as much as practically possible, stays close to the interfaces exposed in Express.js in terms of
core abstractions and the corresponding functions and having to only accommodate a few differences where the nuances
of using Java can not sufficiently accommodate the succinctness or brevity of Javascript.

These core Express abstractions and their corresponding equivalent in Espresso are:

| Express.js  | Espresso jetty |
|-------------|----------------|
| express()   | Espresso       |
| Application | IApplication   |
| Request     | IRequest       |
| Response    | IResponse      |
| Router      | IRouter        | 

## Espresso

## IApplication

## IRequest

## IResponse

## Startup Option

These are defined and documented in the __OptBuilder__ class

1. __host__ - hostname or IP of computer running applications
2. __port__ - tcp port where application is listening for client requests
3. __securePort__ - ssl tcp port where application is listening for client requests
4. __keystorePath__ - file system path to server truststore
5. __keystorePass__ - password for server truststore

