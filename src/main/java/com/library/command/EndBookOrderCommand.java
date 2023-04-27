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

public class EndBookOrderCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(EndBookOrderCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not change order status on ended";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private EndBookOrderCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idBookOrder = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        try {
            BasicBookOrderService.getInstance().changeStatusBookOrderOnEnded(idBookOrder);
            final List<BookOrder> bookOrders = BasicBookOrderService.getInstance().findAllUncompleted();
            request.addAttributeToJsp(Constant.BOOK_ORDER_ATTRIBUTE_NAME, bookOrders);
            return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.librarian.order"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not create book order", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static EndBookOrderCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final EndBookOrderCommand INSTANCE = new EndBookOrderCommand();
    }
}
