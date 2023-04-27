package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Author;
import com.library.service.BasicAuthorService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowAuthorPageCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ShowAuthorPageCommand.class);

    private static final String ERROR_CREATE_BOOK_ORDER_ATTRIBUTE = "could not find all authors";

    private final RequestFactory requestFactory = RequestFactory.getInstance();


    private ShowAuthorPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final List<Author> authors;
        try {
            authors = BasicAuthorService.getInstance().findAll();
            request.addAttributeToJsp(Constant.AUTHORS_ATTRIBUTE_NAME, authors);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.authors"));
        } catch (ServiceException e) {
            LOG.error("Service error, could not find all authors", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_CREATE_BOOK_ORDER_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowAuthorPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowAuthorPageCommand INSTANCE = new ShowAuthorPageCommand();
    }
}
