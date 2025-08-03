package org.dimchik.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionCookieHandler {
    private final String cookieName;
    private final int cookieMaxAge;

    public SessionCookieHandler(String cookieName, int cookieMaxAge) {
        this.cookieName = cookieName;
        this.cookieMaxAge = cookieMaxAge;
    }

    public String extractId(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        for (Cookie cookie : req.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public void set(HttpServletResponse res, String sessionId) {
        Cookie cookie = new Cookie(cookieName, sessionId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath("/");
        res.addCookie(cookie);
    }

    public void clear(HttpServletResponse res) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }
}
