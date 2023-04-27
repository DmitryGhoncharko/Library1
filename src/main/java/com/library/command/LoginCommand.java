package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.library.service.BasicAccountService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    private static final String ERROR_LOGIN_PASS_MASSAGE_ATTRIBUTE = "Incorrect login or password, try again";
    private static final String ERROR_ACCOUNT_PASS_MASSAGE_ATTRIBUTE = "Account error";
    private static final String ERROR_PASS_MASSAGE_ATTRIBUTE = "Login failed, account is not find";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private LoginCommand() {

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if (request.sessionExist() && request.takeFromSession(Constant.ACCOUNT_PARAMETER_NAME).isPresent()) {
            LOG.error("Session not exist or account is absent in session");
            request.addAttributeToJsp(Constant.ERROR_LOGIN_PASS_MESSAGE_NAME, ERROR_ACCOUNT_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.login"));
        }
        final String login = request.getParameter(Constant.LOGIN_PARAMETER_NAME);
        final String password = request.getParameter(Constant.PASSWORD_PARAMETER_NAME);
        final Optional<Account> account;
        try {
            account = BasicAccountService.getInstance().authenticate(login, password);
            if (account.isPresent()) {
                request.clearSession();
                request.createSession();
                request.addAttributeToSession(Constant.ACCOUNT_PARAMETER_NAME, account.get());
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("path.page.index"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_LOGIN_PASS_MESSAGE_NAME, ERROR_LOGIN_PASS_MASSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.login"));
            }
        } catch (ServiceException e) {
            LOG.error("Login failed, account is not find", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static LoginCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final LoginCommand INSTANCE = new LoginCommand();
    }
}
