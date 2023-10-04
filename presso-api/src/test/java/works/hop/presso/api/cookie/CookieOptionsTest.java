package works.hop.presso.api.cookie;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CookieOptionsTest {

    @Test
    void assert_values_found_in_options_match_those_inserted() {
        CookieOptions options = new CookieOptions();
        options.put(CookieOptions.Option.DOMAIN, "localhost");
        assertEquals(options.get(CookieOptions.Option.DOMAIN), "localhost");
        options.put(CookieOptions.Option.MAX_AGE, 20);
        assertEquals(options.get(CookieOptions.Option.MAX_AGE), 20);
        options.put(CookieOptions.Option.HTTP_ONLY, true);
        assertEquals(options.get(CookieOptions.Option.HTTP_ONLY), true);
    }
}