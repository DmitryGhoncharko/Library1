package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.library.service.BasicAccountService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowAccountPageCommand implements Command{

    private static final Logger LOG = LogManager.getLogger(ShowAccountPageCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not find your account!";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowAccountPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long accountId = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));

        try {
            Account account = BasicAccountService.getInstance()
                    .findById(accountId)
                    .orElseThrow(() -> new ServiceException("Service error, could not create book order"));
            request.addAttributeToJsp(Constant.ACCOUNT_PARAMETER_NAME, account);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.account"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not create book order", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowAccountPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowAccountPageCommand INSTANCE = new ShowAccountPageCommand();
    }
}
