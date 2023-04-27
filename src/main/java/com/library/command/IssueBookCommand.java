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

public class IssueBookCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(IssueBookCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not change order status on issued";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private IssueBookCommand() {

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idBookOrder = Long.valueOf(request.getParameter("id"));
        final List<BookOrder> bookOrders;
        try {
            BasicBookOrderService.getInstance().changeStatusBookOrderOnIssued(idBookOrder);
            bookOrders = BasicBookOrderService.getInstance().findAllUncompleted();
            request.addAttributeToJsp("bookOrders", bookOrders);
            return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.librarian.order"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not create book order", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static IssueBookCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final IssueBookCommand INSTANCE = new IssueBookCommand();
    }
}
