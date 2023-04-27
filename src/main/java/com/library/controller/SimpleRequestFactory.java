package com.library.controller;

import com.library.command.CommandRequest;
import com.library.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRequestFactory implements RequestFactory{

    private final Map<String, CommandResponse> responseForwardCash;
    private final Map<String, CommandResponse> responseRedirectCash;


    private SimpleRequestFactory() {
        this.responseForwardCash = new ConcurrentHashMap<>();
        this.responseRedirectCash = new ConcurrentHashMap<>();
    }

    @Override
    public CommandRequest createRequest(HttpServletRequest httpServletRequest) {
        return new WrappingCommandRequest(httpServletRequest);
    }

    @Override
    public CommandResponse createForwardResponse(String path) {
        return responseForwardCash.computeIfAbsent(path, p -> new PlainCommandResponse(p));
    }

    @Override
    public CommandResponse createRedirectResponse(String path) {
        return responseRedirectCash.computeIfAbsent(path, p -> new PlainCommandResponse(true, p));
    }

    public static SimpleRequestFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final SimpleRequestFactory INSTANCE = new SimpleRequestFactory();
    }
}
