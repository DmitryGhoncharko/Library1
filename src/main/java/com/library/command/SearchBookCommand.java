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

import static java.lang.String.format;

public class SearchBookCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(SearchBookCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not find book";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private SearchBookCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String title = request.getParameter(Constant.TITLE_PARAMETER_NAME);
        try {
            final Optional<Book> book = BasicBookService.getInstance().findByTitle(title);
            if (book.isPresent()) {
                return requestFactory.createForwardResponse(format("/controller?command=book_page&id=%s", book.get().getId()));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not search book", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

        public static SearchBookCommand getInstance() {
            return Holder.INSTANCE;
        }

        private static class Holder {
            public static final SearchBookCommand INSTANCE = new SearchBookCommand();
        }
    }
