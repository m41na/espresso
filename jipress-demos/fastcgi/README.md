# Configuring Jetty for fastcgi

1. [Jetty 9 docs](https://eclipse.dev/jetty/documentation/jetty-9/index.html#fastcgi)
2. [Jetty-Users board](https://www.eclipse.org/lists/jetty-users/msg09434.html)

# Start php interpreter

2. [Download php for Windows](https://windows.php.net/download/)
2. Unzip files in a location of your choice - let's assume ```c:\\tools\\php-8.3.1```
3. Add php path to environment variables
4. In a new terminal, start the php-cgi.exe process ```php-cgi.exe -b 127.0.0.1:9000```
5. Find additional [information here](https://www.nginx.com/resources/wiki/start/topics/examples/phpfastcgionwindows/)

The process started in step 4 is a listener that interprets php input and replies to the caller with the result.
In this case, the caller is Jetty

# Start mysql server instance

[Reference docs](https://dev.mysql.com/doc/mysql-linuxunix-excerpt/8.0/en/docker-mysql-getting-started.html)

1. Pull image - ```docker pull container-registry.oracle.com/mysql/community-server```
2. Start
   container - ```docker run --name=local-mysql-server --restart on-failure -p 3306:3306 -d container-registry.oracle.com/mysql/community-server```
3. Retrieve generated password - ```docker logs local-mysql-server 2>&1 | grep GENERATED```
4. Use generated password to sign into container - ```docker exec -it local-mysql-server mysql -uroot -p```
5. Change default password - ```mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '<new password>';```
6. Exec into container to look around - ```docker exec -it local-mysql-server bash```

While signed in as _root_, create a new non-root user for use with installing wordpress

1. create new user (let's assume the name
   mysqluser) - ```mysql> CREATE USER 'mysqluser'@'%' IDENTIFIED BY '<user password>';```
2. grant super user permissions - ```mysql> GRANT ALL PRIVILEGES on *.* to 'mysqluser'@'%';```
3. reload mysql configuration - ```mysql> FLUSH PRIVILEGES;```

## Run a WordPress application

On Windows, some additional steps will be required before continuing.

In the php installation folder:

1. Copy and paste php.ini-production file, changing its name for php.ini (root directory of PHP)
2. Uncomment the following lines

   ;extension=mysqli
   ;extension_dir=ext

3. [Download WordPress](https://wordpress.org/download/)
4. Follow
   the [installation guide](https://developer.wordpress.org/advanced-administration/before-install/howto-install/)

