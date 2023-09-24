package works.hop.presso.jett.cookie;

import jakarta.servlet.http.Cookie;
import works.hop.presso.api.cookie.CookieOptions;

public class CookieBuilder {

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

    private CookieBuilder() {
        //hide constructor
    }

    public static CookieBuilder newBuilder(String name, String value, CookieOptions options) {
        CookieBuilder bld = new CookieBuilder();
        bld.name(name);
        bld.value(value);
        bld.domain((String) options.computeIfAbsent(CookieOptions.Option.DOMAIN, x -> "localhost"));
        bld.encode((Boolean) options.computeIfAbsent(CookieOptions.Option.ENCODE, x -> false));
        bld.expires((String) options.computeIfAbsent(CookieOptions.Option.EXPIRES, x -> "0"));
        bld.httpOnly((Boolean) options.computeIfAbsent(CookieOptions.Option.HTTP_ONLY, x -> true));
        bld.maxAge((int) options.computeIfAbsent(CookieOptions.Option.MAX_AGE, x -> 0));
        bld.path((String) options.get(CookieOptions.Option.PATH));
        bld.priority((String) options.computeIfAbsent(CookieOptions.Option.PRIORITY, x -> ""));
        bld.secure((Boolean) options.computeIfAbsent(CookieOptions.Option.SECURE, x -> true));
        bld.signed((Boolean) options.computeIfAbsent(CookieOptions.Option.SIGNED, x -> false));
        bld.sameSite((String) options.computeIfAbsent(CookieOptions.Option.SAME_SIGHT, x -> ""));
        bld.comment((String) options.computeIfAbsent(CookieOptions.Option.COMMENT, x -> ""));
        bld.version((int) options.computeIfAbsent(CookieOptions.Option.VERSION, x -> 1));
        return bld;
    }

    public CookieBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieBuilder encode(Boolean encode) {
        this.encode = encode;
        return this;
    }

    public CookieBuilder expires(String expires) {
        this.expires = expires;
        return this;
    }

    public CookieBuilder httpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public CookieBuilder maxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder priority(String priority) {
        this.priority = priority;
        return this;
    }

    public CookieBuilder secure(Boolean secure) {
        this.secure = secure;
        return this;
    }

    public CookieBuilder signed(Boolean signed) {
        this.signed = signed;
        return this;
    }

    public CookieBuilder sameSite(String sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    public CookieBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CookieBuilder value(String value) {
        this.value = value;
        return this;
    }

    public CookieBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public CookieBuilder version(int version) {
        this.version = version;
        return this;
    }

    public Cookie build() {
        Cookie cookie = new Cookie(this.name, this.value);
        cookie.setPath(this.path);
        cookie.setHttpOnly(this.httpOnly);
        cookie.setMaxAge(this.maxAge);
        cookie.setComment(this.comment);
        cookie.setDomain(this.domain);
        cookie.setSecure(this.secure);
        cookie.setVersion(this.version);
        return cookie;
    }
}
