package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Book;
import com.library.service.BasicBookService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ShowBookPageCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ShowBookPageCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not find a book";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowBookPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long id = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        try {
            final Book book = BasicBookService.getInstance().findById(id).orElseThrow(() -> new ServiceException("Service error, could not find a book"));
            request.addAttributeToJsp(Constant.BOOK_PARAMETER_NAME, book);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.book"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not find book order", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowBookPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowBookPageCommand INSTANCE = new ShowBookPageCommand();
    }
}
