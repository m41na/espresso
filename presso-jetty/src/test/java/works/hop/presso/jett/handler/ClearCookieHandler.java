package works.hop.presso.jett.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClearCookieHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        baseRequest.setHandled(true);
        response.setStatus(200);
        Cookie[] cookies = request.getCookies();
        Map<String, Object> map = Arrays.stream(Optional.ofNullable(cookies).orElse(new Cookie[0]))
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue, (x, y) -> x));

        //send expired cookie
        Cookie cookie = new Cookie("name", null);
        cookie.setMaxAge(0);
        cookie.setPath("/cookie");
        response.addCookie(cookie);

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().printf("{\"value\":\"%s\"}", map.get("name"));
    }
}
