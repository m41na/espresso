package com.akilisha.espresso.jett.cookie;

import com.akilisha.espresso.api.cookie.CookieOptions;
import jakarta.servlet.http.Cookie;
import org.eclipse.jetty.server.Request;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class CookieBuilder {

    String domain;
    Function<Request, String> domainResolver = request -> {
        if (this.domain == null) {
            return request.getRemoteHost();
        }
        return this.domain;
    };
    String expires;
    Function<Integer, String> expiresResolver = current -> {
        if (this.expires == null) {
            Date expiry = new Date();
            expiry.setTime(expiry.getTime() + (current * 60 * 1000)); //30 minutes
            DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzzz");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format.format(expiry);
        }
        return this.expires;
    };
    Integer maxAge;
    BiFunction<TimeUnit, Integer, Integer> maxAgeResolver = (timeUnit, duration) -> {
        if (this.maxAge == null) {
            return (int) timeUnit.convert(duration, TimeUnit.MICROSECONDS);
        }
        return this.maxAge;
    };
    String path;
    Function<String, String> urlEncoder = current -> {
        if (this.path == null) {
            return URLEncoder.encode(current, StandardCharsets.UTF_8);
        }
        return this.path;
    };
    Function<Request, String> pathResolver = (req) -> {
        if (this.path == null) {
            return req.getPathInfo();
        }
        return urlEncoder.apply(this.path);
    };
    Boolean httpOnly;
    String priority;
    Boolean secure;
    Boolean signed;
    String sameSite;
    String name;
    String value;
    String comment;
    Integer version;
    TimeUnit timeUnit;

    private CookieBuilder() {
        //hide constructor
    }

    public static CookieBuilder newBuilder(String name, String value, CookieOptions options) {
        CookieBuilder bld = new CookieBuilder();
        bld.name(name);
        bld.value(value);
        bld.domain((String) options.get(CookieOptions.Option.DOMAIN));
        bld.expires((String) options.get(CookieOptions.Option.EXPIRES));
        bld.maxAge(bld.timeUnit, (int) options.get(CookieOptions.Option.MAX_AGE));
        bld.path((String) options.get(CookieOptions.Option.PATH));
        bld.httpOnly((Boolean) options.get(CookieOptions.Option.HTTP_ONLY));
        bld.priority((String) options.get(CookieOptions.Option.PRIORITY));
        bld.secure((Boolean) options.get(CookieOptions.Option.SECURE));
        bld.signed((Boolean) options.get(CookieOptions.Option.SIGNED));
        bld.sameSite((String) options.get(CookieOptions.Option.SAME_SIGHT));
        bld.comment((String) options.get(CookieOptions.Option.COMMENT));
        bld.version((int) options.get(CookieOptions.Option.VERSION));
        return bld;
    }

    public CookieBuilder domain(String domain) {
        this.domain = domain;
        return this;
    }

    public CookieBuilder expires(String expires) {
        this.expires = expires;
        return this;
    }

    public CookieBuilder maxAge(TimeUnit timeUnit, int maxAge) {
        this.timeUnit = timeUnit;
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder httpOnly(Boolean httpOnly) {
        this.httpOnly = httpOnly;
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

    public Cookie build(Request request) {
        Cookie cookie = new Cookie(
                requireNonNull(this.name, "Cookie name is a required attribute"),
                requireNonNull(this.value, "Cookie value is a required attribute"));
        cookie.setPath(pathResolver.apply(request));
        cookie.setDomain(this.domainResolver.apply(request));
        cookie.setMaxAge(maxAgeResolver.apply(TimeUnit.MINUTES, this.maxAge));
        if (this.httpOnly != null) {
            cookie.setHttpOnly(this.httpOnly);
        }
        if (this.comment != null) {
            cookie.setComment(this.comment);
        }
        if (this.secure != null) {
            cookie.setSecure(this.secure);
        }
        if (this.version != null) {
            cookie.setVersion(this.version);
        }
        return cookie;
    }
}
