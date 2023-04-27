package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Book;
import com.library.service.BasicBookService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ShowCreateBookOrderPageCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ShowCreateBookOrderPageCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "could not find book";
    private static final String ERROR_FAILED_PASS_MESSAGE_ATTRIBUTE = "failed find book";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowCreateBookOrderPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idBook = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        try {
            final Optional<Book> book = BasicBookService.getInstance().findById(idBook);
            if (book.isPresent()) {
                request.addAttributeToJsp(Constant.BOOK_PARAMETER_NAME, book.get());
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.create.order"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not find book", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_FAILED_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowCreateBookOrderPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowCreateBookOrderPageCommand INSTANCE = new ShowCreateBookOrderPageCommand();
    }
}
