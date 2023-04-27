package com.library.command;

import com.library.controller.RequestFactory;
import com.library.util.ConfigurationManager;

public class LogoutCommand implements Command {

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private LogoutCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        request.clearSession();
        return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("path.page.index"));
    }

    public static LogoutCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
