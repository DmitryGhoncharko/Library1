package com.library.filter;

import com.library.util.Constant;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class LangFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (!fetchCookie(request).isPresent()) {
            final Cookie cookieDefaultLang = new Cookie(Constant.COOKIE_PARAMETER_NAME, Constant.COOKIE_EN_LANG_VALUE);
            response.addCookie(cookieDefaultLang);
        }
        final String lang = request.getParameter(Constant.COOKIE_PARAMETER_NAME);
        if (lang != null) {
            final Cookie cookie = fetchCookie(request).get();
            if (!cookie.getValue().equals(lang)) {
                if (lang.equals(Constant.COOKIE_EN_LANG_VALUE)) {
                    cookie.setValue(Constant.COOKIE_EN_LANG_VALUE);
                } else {
                    cookie.setValue(Constant.COOKIE_RU_LANG_VALUE);
                }
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<Cookie> fetchCookie(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                final String cookieName = cookie.getName();
                if (cookieName.equals(Constant.COOKIE_PARAMETER_NAME) && cookieName != null) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }
}
