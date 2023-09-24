package works.hop.presso.api.cookie;

public class CookieOptionsBuilder {

    String domain;
    Boolean encode;
    String expires;
    Boolean httpOnly = true;
    int maxAge;
    String path;
    String priority;
    Boolean secure = true;
    Boolean signed;
    String sameSite;
    String name;
    String value;
    String comment;
    int version;

    private CookieOptionsBuilder() {
        //hide constructor
    }

    public static CookieOptionsBuilder newBuilder() {
        return new CookieOptionsBuilder();
    }

    public CookieOptionsBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieOptionsBuilder encode(Boolean encode) {
        this.encode = encode;
        return this;
    }

    public CookieOptionsBuilder expires(String expires) {
        this.expires = expires;
        return this;
    }

    public CookieOptionsBuilder httpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public CookieOptionsBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CookieOptionsBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieOptionsBuilder priority(String priority) {
        this.priority = priority;
        return this;
    }

    public CookieOptionsBuilder secure(Boolean secure) {
        this.secure = secure;
        return this;
    }

    public CookieOptionsBuilder signed(Boolean signed) {
        this.signed = signed;
        return this;
    }

    public CookieOptionsBuilder sameSite(String sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    public CookieOptionsBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CookieOptionsBuilder value(String value) {
        this.value = value;
        return this;
    }

    public CookieOptionsBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public CookieOptionsBuilder version(int version) {
        this.version = version;
        return this;
    }

    public CookieOptions build() {
        CookieOptions options = new CookieOptions();
        options.put(CookieOptions.Option.DOMAIN, this.domain);
        options.put(CookieOptions.Option.ENCODE, this.encode);
        options.put(CookieOptions.Option.EXPIRES, this.expires);
        options.put(CookieOptions.Option.HTTP_ONLY, this.httpOnly);
        options.put(CookieOptions.Option.MAX_AGE, this.maxAge);
        options.put(CookieOptions.Option.PATH, this.path);
        options.put(CookieOptions.Option.PRIORITY, this.priority);
        options.put(CookieOptions.Option.SECURE, this.secure);
        options.put(CookieOptions.Option.SIGNED, this.signed);
        options.put(CookieOptions.Option.SAME_SIGHT, this.sameSite);
        options.put(CookieOptions.Option.COMMENT, this.comment);
        options.put(CookieOptions.Option.VERSION, this.version);
        return options;
    }
}
