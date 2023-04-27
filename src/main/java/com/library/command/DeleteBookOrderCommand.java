package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Account;
import com.library.model.Role;
import com.library.service.BasicBookOrderService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.lang.String.format;

public class DeleteBookOrderCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(DeleteBookOrderCommand.class);

    private static final String URL_READER_BOOK_ORDER_PAGE = "controller?command=reader_book_order_page&id=%s";
    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not delete book order";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private DeleteBookOrderCommand() {

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idBookOrder = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        final Optional<Object> accountSession = request.takeFromSession(Constant.ACCOUNT_PARAMETER_NAME);
        final Account account = (Account) accountSession.get();
        final Role role = account.getRole();
        try {
            if (BasicBookOrderService.getInstance().delete(idBookOrder)) {
                if (Role.READER.equals(role)) {
                    return requestFactory.createRedirectResponse(format(URL_READER_BOOK_ORDER_PAGE, account.getId()));
                } else {
                    return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.librarian.order"));
                }
            }
        } catch (ServiceException e) {
            LOG.error("could not delete book order", e);

        }
        request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
        return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
    }

    public static DeleteBookOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final DeleteBookOrderCommand INSTANCE = new DeleteBookOrderCommand();
    }
}
