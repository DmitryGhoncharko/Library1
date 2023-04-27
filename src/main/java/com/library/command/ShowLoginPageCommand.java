package com.library.command;

import com.library.controller.RequestFactory;
import com.library.util.ConfigurationManager;

public class ShowLoginPageCommand implements Command{

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowLoginPageCommand() {}

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.login"));
    }

    public static ShowLoginPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowLoginPageCommand INSTANCE = new ShowLoginPageCommand();
    }
}
