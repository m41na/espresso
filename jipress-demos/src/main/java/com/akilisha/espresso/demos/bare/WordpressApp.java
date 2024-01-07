package com.akilisha.espresso.demos.bare;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.fcgi.server.proxy.FastCGIProxyServlet;
import org.eclipse.jetty.fcgi.server.proxy.TryFilesFilter;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import java.util.EnumSet;

public class WordpressApp {

    public static Server createServer() {
        Server server = new Server();
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);
        try (ServerConnector connector = new ServerConnector(server, http11)) {
            connector.setPort(9080);
            server.setConnectors(new Connector[]{connector});
        }
        return server;
    }

    public static void main(String[] args) throws Exception {
        // String resourceRoot = "<PROJECT_FOLDER>\\jipress-demos\\fastcgi";
        String resourceRoot = "C:\\Projects\\wordpress";
        Server server = createServer();

        // servlet context
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.setBaseResource(Resource.newResource(resourceRoot));
        context.setWelcomeFiles(new String[]{"index.html", "index.php"});

        // add and configure proxy filter
        FilterHolder filterHolder = context.addFilter(TryFilesFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        // Configure the filter.
        filterHolder.setInitParameter("files", "$path /index.php?p=$path");
        filterHolder.setAsyncSupported(true);

        // add and configure default servlet
        ServletHolder defaultHolder = context.addServlet(DefaultServlet.class, "/");
        defaultHolder.setInitParameter("dirAllowed", "false");
        defaultHolder.setInitParameter("gzip", "true");

        // add and configure fastcgi servlet
        ServletHolder cgiHolder = context.addServlet(FastCGIProxyServlet.class, "*.php");
        cgiHolder.setInitParameter("proxyTo", "http://localhost:9000");
        cgiHolder.setInitParameter("prefix", "/");
        // cgiHolder.setInitParameter("scriptRoot", Paths.get(resourceRoot, "wordpress").toString());
        cgiHolder.setInitParameter("scriptRoot", resourceRoot);
        cgiHolder.setInitParameter("scriptPattern", "(.+?\\.php)");
        cgiHolder.setInitOrder(1);
        cgiHolder.setAsyncSupported(true);

        // start server
        server.setHandler(context);
        server.start();
    }
}
