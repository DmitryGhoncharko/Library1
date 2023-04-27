package com.library.command;

import com.library.controller.RequestFactory;
import com.library.util.ConfigurationManager;

public class ShowMainPageCommand implements Command {

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowMainPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.main"));
    }

    public static ShowMainPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowMainPageCommand INSTANCE = new ShowMainPageCommand();
    }
}
