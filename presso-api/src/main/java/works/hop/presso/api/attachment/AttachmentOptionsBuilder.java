package works.hop.presso.api.attachment;

import java.util.Collections;
import java.util.Map;

public class AttachmentOptionsBuilder {

    String root;
    Boolean lastModified = true;
    Map<String, String> headers = Collections.emptyMap();
    Boolean cacheControl = true;
    int maxAge = 0;
    DotFiles dotFiles = DotFiles.ignore;
    Boolean acceptRanges = true;
    Boolean immutable = false;

    private AttachmentOptionsBuilder() {
        //hide constructor
    }

    public static AttachmentOptionsBuilder newBuilder() {
        return new AttachmentOptionsBuilder();
    }

    public AttachmentOptionsBuilder root(String root) {
        this.root = root;
        return this;
    }

    public AttachmentOptionsBuilder lastModified(Boolean lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public AttachmentOptionsBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public AttachmentOptionsBuilder cacheControl(Boolean cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public AttachmentOptionsBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public AttachmentOptionsBuilder dotFiles(DotFiles dotFiles) {
        this.dotFiles = dotFiles;
        return this;
    }

    public AttachmentOptionsBuilder acceptRanges(Boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
        return this;
    }

    public AttachmentOptionsBuilder immutable(Boolean immutable) {
        this.immutable = immutable;
        return this;
    }

    public AttachmentOptions build() {
        AttachmentOptions options = new AttachmentOptions();
        options.put(AttachmentOptions.Option.CACHE_CONTROL, this.cacheControl);
        options.put(AttachmentOptions.Option.LAST_MODIFIED, this.lastModified);
        options.put(AttachmentOptions.Option.ROOT, this.root);
        options.put(AttachmentOptions.Option.ACCEPT_RANGES, this.acceptRanges);
        options.put(AttachmentOptions.Option.MAX_AGE, this.maxAge);
        options.put(AttachmentOptions.Option.DOT_FILES, this.dotFiles);
        options.put(AttachmentOptions.Option.IMMUTABLE, this.immutable);
        options.put(AttachmentOptions.Option.HEADERS, this.headers);
        return options;
    }

    public enum DotFiles {ignore, allow, deny}
}
