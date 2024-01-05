-pluginsHome
D:\Projects\java\gradle\espresso
-viewPlugins
plugins-views/build/libs
-contentPlugins
plugins-content/build/libs
-routerPlugins
plugins-routable/build/libs

Running docker container
```bash
docker run --rm ^
    -p 3000:3000 ^
    -v C:\Projects\java\espresso\presso-demos\www:/app/www ^
    -v C:\Projects\java\espresso\cert:/app/cert ^
    -v C:\Projects\java\espresso\plugins-routable\build\libs:/app/plugins/router ^
    jipress-jetty:latest
 ```

tag image before pushing
```bash
docker tag jipress-jetty:latest <dockerhost>/<username>/espresso<:tag>
```

push image to docker.io
```bash
docker login
docker push <dockerhost>/<username>/espresso<:tag>
```