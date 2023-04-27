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

public class ShowUpdateBookPageCommand implements Command{

    private static final Logger LOG = LogManager.getLogger(ShowUpdateBookPageCommand.class);

    private static final String ERROR_PASS_MASSAGE_ATTRIBUTE = "Could not find book";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowUpdateBookPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idBook = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        final Optional<Book> book;
        try {
            book = BasicBookService.getInstance().findById(idBook);
            request.addAttributeToJsp(Constant.BOOK_PARAMETER_NAME, book.get());
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.update.book"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not find  update book", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MASSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowUpdateBookPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowUpdateBookPageCommand INSTANCE = new ShowUpdateBookPageCommand();
    }
}
