package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.library.service.BasicAccountService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowAccountsPageCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ShowAccountsPageCommand.class);

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowAccountsPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Account> accounts;
        try {
            accounts = BasicAccountService.getInstance().findAll();
            request.addAttributeToJsp(Constant.ACCOUNTS_ATTRIBUTE_NAME, accounts);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.accounts"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not read accounts", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, "Could not find accounts!");
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowAccountsPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowAccountsPageCommand INSTANCE = new ShowAccountsPageCommand();
    }
}
