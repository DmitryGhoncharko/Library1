package com.library.command;

public interface CommandResponse {

    boolean isRedirect();

    String getPath();

}
