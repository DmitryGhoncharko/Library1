package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Book;
import com.library.service.BasicBookService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowCatalogPageCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(ShowCatalogPageCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Could not find books";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private ShowCatalogPageCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        try {
            if (!BasicBookService.getInstance().findAll().isEmpty()) {
                final List<Book> books = BasicBookService.getInstance().findAll();
                request.addAttributeToJsp(Constant.BOOKS_ATTRIBUTE_NAME, books);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.catalog"));
            } else {
                request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not create book order", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static ShowCatalogPageCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ShowCatalogPageCommand INSTANCE = new ShowCatalogPageCommand();
    }
}
