package com.akilisha.espresso.api.attachment;

import java.util.EnumMap;

public class AttachmentOptions extends EnumMap<AttachmentOptions.Option, Object> {

    public AttachmentOptions() {
        super(AttachmentOptions.Option.class);
    }

    public enum Option {

        MAX_AGE("maxAge", "Convenient option for setting the expiry time relative to the current time in milliseconds."),
        ROOT("root", "Root directory for relative filenames.."),
        LAST_MODIFIED("lastModified", "Sets the Last-Modified header to the last modified date of the file on the OS. Set false to disable it."),
        HEADERS("headers", "Object containing HTTP headers to serve with the file. The header Content-Disposition will be overridden by the filename argument."),
        DOT_FILES("dotfiles", "Option for serving dotfiles. Possible values are 'allow', 'deny', 'ignore'."),
        ACCEPT_RANGES("acceptRanges", "Enable or disable accepting ranged requests."),
        CACHE_CONTROL("cacheControl", "Enable or disable setting Cache-Control response header."),
        IMMUTABLE("immutable", "Enable or disable the immutable directive in the Cache-Control response header.");

        final String name;
        final String description;

        Option(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
