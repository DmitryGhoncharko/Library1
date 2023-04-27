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

public class UpdateBookCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(UpdateBookCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not update book";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private UpdateBookCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final Long idBook = Long.valueOf(request.getParameter(Constant.ID_PARAMETER_NAME));
        final String title = request.getParameter(Constant.TITLE_PARAMETER_NAME);
        final String datePublished = request.getParameter(Constant.DATE_PUBLISHED_PARAMETER_NAME);
        final Integer amountOfLeft = Integer.valueOf(request.getParameter(Constant.AMOUNT_OF_LEFT_PARAMETER_NAME));
        final Optional<Book> updateBook;
        try {
            updateBook = BasicBookService.getInstance().update(idBook, title, datePublished, amountOfLeft);
            if (updateBook.isPresent()) {
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.catalog"));
            }else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not update book", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static UpdateBookCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final UpdateBookCommand INSTANCE = new UpdateBookCommand();
    }
}
