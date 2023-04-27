package com.library.command;

import com.library.controller.RequestFactory;
import com.library.util.ConfigurationManager;

public class ShowRegistrationPageCommand implements Command{

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowRegistrationPageCommand() {}

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.register"));
    }

    public static ShowRegistrationPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowRegistrationPageCommand INSTANCE = new ShowRegistrationPageCommand();
    }
}
