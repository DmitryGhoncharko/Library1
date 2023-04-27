package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.BookOrder;
import com.library.service.BasicBookOrderService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowLibrarianBookOrderPageCommand implements Command{

    private static final Logger LOG = LogManager.getLogger(ShowLibrarianBookOrderPageCommand.class);

    private static final String ERROR_PASS_MASSAGE_ATTRIBUTE = "There is no uncompleted orders!";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowLibrarianBookOrderPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<BookOrder> bookOrders;
        try {
            bookOrders = BasicBookOrderService.getInstance().findAllUncompleted();
            request.addAttributeToJsp(Constant.BOOK_ORDERS_ATTRIBUTE_NAME, bookOrders);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.librarian.order"));
        } catch (ServiceException e) {
            LOG.error("could not create book order", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowLibrarianBookOrderPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowLibrarianBookOrderPageCommand INSTANCE = new ShowLibrarianBookOrderPageCommand();
    }
}
