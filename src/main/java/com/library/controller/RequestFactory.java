package com.library.controller;

import com.library.command.CommandRequest;
import com.library.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {

    CommandRequest createRequest(HttpServletRequest httpServletRequest);

    CommandResponse createForwardResponse(String path);

    CommandResponse createRedirectResponse(String path);

    static RequestFactory getInstance() {
        return SimpleRequestFactory.getInstance();
    }

}
