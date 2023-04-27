package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.library.service.BasicAccountService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;

import java.util.Optional;

public class RegistrationCommand implements Command {

    private static final String ERROR_INCORRECT_PASS_MESSAGE_ATTRIBUTE = "Incorrect data, please try again";
    private static final String  ERROR_PASS_MESSAGE_ATTRIBUTE = "Registration failed";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private RegistrationCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(Constant.LOGIN_PARAMETER_NAME);
        final String password = request.getParameter(Constant.PASSWORD_PARAMETER_NAME);
        final String firstName = request.getParameter(Constant.FIRST_NAME_PARAMETER_NAME);
        final String lastName = request.getParameter(Constant.LAST_NAME_PARAMETER_NAME);
        try {
            final Optional<Account> account = BasicAccountService.getInstance().create(login, password, firstName, lastName);
            if (account.isPresent()) {
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("path.page.index"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_REGISTER_PASS_MESSAGE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.register"));
            }
        } catch (ServiceException e) {
            request.addAttributeToJsp(Constant.ERROR_REGISTER_PASS_MESSAGE_NAME, ERROR_INCORRECT_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.register"));
        }
    }

    public static RegistrationCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final RegistrationCommand INSTANCE = new RegistrationCommand();
    }
}
