package com.library.command;

import java.util.Optional;

public interface CommandRequest {

    String getParameter(String name);

    String[] getParameterValues(String name);

    String getRequestURI();

    void addAttributeToJsp(String name, Object attribute);

    void createSession();

    boolean addAttributeToSession(String name, Object attribute);

    void clearSession();

    Optional<Object> takeFromSession(String name);

    boolean sessionExist();

}
