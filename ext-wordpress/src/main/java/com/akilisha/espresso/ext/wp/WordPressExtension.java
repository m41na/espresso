package com.akilisha.espresso.ext.wp;

import com.akilisha.espresso.api.extension.IExtension;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;

import static com.akilisha.espresso.ext.wp.config.FastCgiConfigurer.*;

public class WordPressExtension implements IExtension {

    String contextPath = "/";
    String resourceRoot = System.getProperty("WORDPRESS_HOME");

    @Override
    public String contextPath() {
        return this.contextPath;
    }

    @Override
    public String resourceRoot() {
        return this.resourceRoot;
    }

    @Override
    public void resourceRoot(String resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

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