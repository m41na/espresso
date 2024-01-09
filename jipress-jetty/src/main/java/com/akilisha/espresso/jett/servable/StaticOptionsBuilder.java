package com.akilisha.espresso.jett.servable;

import com.akilisha.espresso.api.servable.IStaticOptions;
import com.akilisha.espresso.api.servable.IStaticOptionsBuilder;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import java.io.IOException;
import java.util.function.Function;

public class StaticOptionsBuilder {

    IStaticOptions staticOptions;

    private StaticOptionsBuilder() {
        //hide constructor
    }

    public static StaticOptionsBuilder newBuilder() {
        return new StaticOptionsBuilder();
    }

    public StaticOptionsBuilder options(IStaticOptions options) {
        this.staticOptions = options;
        return this;
    }

    public StaticOptionsBuilder options(Function<IStaticOptionsBuilder, IStaticOptions> builder) {
        return this.options(builder.apply(IStaticOptionsBuilder.newBuilder()));
    }

    public ResourceHandler build() {
        try {
            ResourceHandler handler = new ResourceHandler();
            // Configure resources base directory
            handler.setBaseResource(Resource.newResource(this.staticOptions.baseDirectory()));
            // Configure directory listing.
            handler.setDirectoriesListed(this.staticOptions.listDirectories());
            // Configure welcome files.
            handler.setWelcomeFiles(this.staticOptions.welcomeFiles());
            // Configure whether to accept range requests.
            handler.setAcceptRanges(this.staticOptions.acceptRanges());
            return handler;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
