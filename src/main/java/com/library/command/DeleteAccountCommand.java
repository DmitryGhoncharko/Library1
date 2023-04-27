package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.service.BasicAccountService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteAccountCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(DeleteAccountCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not delete account";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteAccountCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idAccount = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        try {
            if (BasicAccountService.getInstance().delete(idAccount)) {
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.account"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not account", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static DeleteAccountCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final DeleteAccountCommand INSTANCE = new DeleteAccountCommand();
    }
}
