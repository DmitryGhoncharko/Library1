package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.epam.jwd.library.service.*;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import com.library.service.BasicBookOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.lang.String.format;

public class CreateBookOrderCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(CreateBookOrderCommand.class);

    private static final String ERROR_CREATE_BOOK_ORDER_MASSAGE = "errorCreateBookOrderMassage";
    private static final String ERROR_PASS_MASSAGE = "errorPassMessage";
    private static final String ERROR_ACCOUNT_SESSION_ATTRIBUTE = "Account is not present in session or check checkbox";
    private static final String ERROR_CREATE_BOOK_ORDER_ATTRIBUTE = "Could nor create book";

    private final BasicBookOrderService bookOrderService = BasicBookOrderService.getInstance();
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateBookOrderCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Optional<Object> accountSession = request.takeFromSession(Constant.ACCOUNT_PARAMETER_NAME);
        final Long idBook = Long.valueOf(request.getParameter(Constant.BOOK_ID_PARAMETER_NAME));
        final String[] orderTypes = request.getParameterValues(Constant.ORDER_TYPE_PARAMETER_NAME);
        try {
            if (!accountSession.isPresent() && (orderTypes.length > 1)) {
                request.addAttributeToJsp(ERROR_PASS_MASSAGE, ERROR_ACCOUNT_SESSION_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
                final Account account = (Account) accountSession.get();
                final String orderType = orderTypes[0];
                bookOrderService.createBookOrder(account, idBook, orderType).orElseThrow(() -> new ServiceException("Could not create book order")); {
                    return requestFactory.createRedirectResponse(format(ConfigurationManager.getProperty("url.reader.order") + account.getId()));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not create book order", e);
            request.addAttributeToJsp(ERROR_CREATE_BOOK_ORDER_MASSAGE, ERROR_CREATE_BOOK_ORDER_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static CreateBookOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final CreateBookOrderCommand INSTANCE = new CreateBookOrderCommand();
    }
}
