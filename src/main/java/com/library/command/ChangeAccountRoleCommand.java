package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.service.BasicAccountService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.String.format;

public class ChangeAccountRoleCommand implements Command{

    private static final Logger LOG = LogManager.getLogger(ChangeAccountRoleCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not change role in account";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    public ChangeAccountRoleCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long accountId = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        final String roleName = request.getParameter(Constant.ROLE_PARAMETER_NAME);
        try {
            if (BasicAccountService.getInstance().changeRole(accountId, roleName)) {
                return requestFactory.createRedirectResponse(format(ConfigurationManager.getProperty("url.account") + "&id=%s", accountId));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not change role from account", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ChangeAccountRoleCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ChangeAccountRoleCommand INSTANCE = new ChangeAccountRoleCommand();
    }
}
