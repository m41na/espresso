package works.hop.presso.api.cookie;

import java.util.EnumMap;

public class CookieOptions extends EnumMap<CookieOptions.Option, Object> {

    public CookieOptions() {
        super(Option.class);
    }

    public enum Option {

        DOMAIN("domain", "Domain name for the cookie. Defaults to the domain name of the app."),
        ENCODE("encode", "A synchronous function used for cookie value encoding. Defaults to encodeURIComponent."),
        EXPIRES("expires", "Expiry date of the cookie in GMT. If not specified or set to 0, creates a session cookie."),
        HTTP_ONLY("httpOnly", "Flags the cookie to be accessible only by the web server."),
        MAX_AGE("maxAge", "Convenient option for setting the expiry time relative to the current time in milliseconds."),
        PATH("path", "Path for the cookie. Defaults to '/'."),
        PRIORITY("priority", "Value of the 'Priority' Set-Cookie attribute."),
        SECURE("secure", "Marks the cookie to be used with HTTPS only."),
        SIGNED("signed", "Indicates if the cookie should be signed."),
        SAME_SIGHT("sameSite", "Value of the 'SameSite' Set-Cookie attribute."),
        COMMENT("comment", "Value of the cookie's 'Comment'."),
        VERSION("version", "Value of the cookie's 'Version'."),
        TIME_UNIT("timeUnit", "input unit for time when specifying max age - defaults to MINUTES");

        final String name;
        final String description;

        Option(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
