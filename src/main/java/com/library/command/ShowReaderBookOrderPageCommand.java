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

public class ShowReaderBookOrderPageCommand implements Command{

    private static final Logger LOG = LogManager.getLogger(ShowReaderBookOrderPageCommand.class);

    private static final String ERROR_PASS_MASSAGE_ATTRIBUTE = "You have not order!";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowReaderBookOrderPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        final List<BookOrder> bookOrders;
        try {
            bookOrders = BasicBookOrderService.getInstance().findOrdersByIdAccount(id);
            request.addAttributeToJsp("bookOrders", bookOrders);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.reader.order"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not find reader by id", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowReaderBookOrderPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowReaderBookOrderPageCommand INSTANCE = new ShowReaderBookOrderPageCommand();
    }
}
