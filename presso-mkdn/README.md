## CMD and ENTRYPOINT

When you use both ENTRYPOINT and CMD in a Dockerfile, the command section will be appended to the entrypoint executable 
as arguments. Thus considering the following example:

```bash
ENTRYPOINT ["/start.sh"]
CMD ["aptly", "api", "serve"]
```

It is equivalent to running:

```ENTRYPOINT["/start.sh", "aptly", "api", "serve"]```

Bear in mind that to use ENTRYPOINT and CMD together, you need to specify both in the array format. Doing something 
like this WILL NOT WORK:

```bash
ENTRYPOINT ./my_script.sh
CMD echo "hello world"
```

To override the ENTRYPOINT and its arguments when invoking *docker run* you will need to explicitly use the 
*--entrypoint* flag:

Images can only have one ENTRYPOINT. If you repeat the Dockerfile instruction more than once, the last one will apply. 
When an image is created without an ENTRYPOINT, Docker defaults to using /bin/sh -c.

When you have command line arguments that need to be resolved when starting a container, you need to use the non-array 
syntax so that bash can expand those properties

```bash
ENV HOME=/opt/app/www
ENV PAGES=pages
ENV WELCOME=index-1.html
ENV PORT=9080
ENV HOST=0.0.0.0
...
...
CMD java -jar app.jar --home $HOME --pages $PAGES --welcome $WELCOME -port $PORT -host $HOST
```

In doing this, you can then override any ENV argument when starting the container

```docker run -d -p 9991:9991 -v .\www:/opt/app/www -e PORT=9991 java-mkdn-web:latest```

**Pay careful attention** to the use of *"./www"* vs *".\www"* when mapping the volume drives. It makes a world of a 
difference depending on what kind of terminal you are executing the ```dockr run``` command. 