package com.library.controller;

import com.library.command.CommandRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class WrappingCommandRequest implements CommandRequest {

    private final HttpServletRequest httpServletRequest;

    public WrappingCommandRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void addAttributeToJsp(String name, Object attribute) {
        httpServletRequest.setAttribute(name, attribute);
    }

    public String getParameter(String name) {
        final String parameter = httpServletRequest.getParameter(name);
        return parameter;
    }

    @Override
    public String[] getParameterValues(String name) {
        final String[] parameters = httpServletRequest.getParameterValues(name);
        return parameters;
    }

    @Override
    public String getRequestURI() {
        return httpServletRequest.getRequestURI();
    }

    @Override
    public void createSession() {
        httpServletRequest.getSession();
    }

    @Override
    public boolean addAttributeToSession(String name, Object attribute) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.setAttribute(name, attribute);
            return true;
        }
        return false;
    }

    @Override
    public void clearSession() {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public Optional<Object> takeFromSession(String name) {
        final HttpSession session = httpServletRequest.getSession(false);
        final Optional<HttpSession> sessionOptional = Optional.ofNullable(session);
        return sessionOptional.map(s -> s.getAttribute(name));
    }

    @Override
    public boolean sessionExist() {
        return httpServletRequest.getSession(false) != null;
    }
}
