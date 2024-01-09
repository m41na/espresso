package com.akilisha.espresso.ext.wp.config;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.fcgi.server.proxy.FastCGIProxyServlet;
import org.eclipse.jetty.fcgi.server.proxy.TryFilesFilter;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.io.IOException;
import java.util.EnumSet;

public class FastCgiConfigurer {
    public static ServletContextHandler createServletContext(String contextPath, String resourceRoot) throws IOException {
        // servlet context
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(contextPath);
        context.setBaseResource(Resource.newResource(resourceRoot));
        context.setWelcomeFiles(new String[]{"index.html", "index.php"});
        return context;
    }

    public static void configureTryFilesFilter(ServletContextHandler context) {
        // add and configure proxy filter
        FilterHolder filterHolder = context.addFilter(TryFilesFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        // Configure the filter.
        filterHolder.setInitParameter("files", "$path /index.php?p=$path");
        filterHolder.setAsyncSupported(true);
    }

    public static void configureDefaultServletHandler(ServletContextHandler context) {
        // add and configure default servlet
        ServletHolder defaultHolder = context.addServlet(DefaultServlet.class, "/");
        defaultHolder.setInitParameter("dirAllowed", "false");
        defaultHolder.setInitParameter("gzip", "true");
    }

    public static void configureFastCgiHandler(ServletContextHandler context, String resourceRoot) {
        // add and configure fastcgi servlet
        ServletHolder cgiHolder = context.addServlet(FastCGIProxyServlet.class, "*.php");
        cgiHolder.setInitParameter("proxyTo", "http://localhost:9000");
        cgiHolder.setInitParameter("prefix", "/");
        // cgiHolder.setInitParameter("scriptRoot", Paths.get(resourceRoot, "wordpress").toString());
        cgiHolder.setInitParameter("scriptRoot", resourceRoot);
        cgiHolder.setInitParameter("scriptPattern", "(.+?\\.php)");
        cgiHolder.setInitOrder(1);
        cgiHolder.setAsyncSupported(true);
    }
}
