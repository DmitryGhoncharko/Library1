package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.service.BasicBookService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateBookCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(CreateBookCommand.class);

    private static final String ERROR_CREAT_BOOK_MESSAGE_NAME = "errorCreatBookMessage";
    private static final String ERROR_INCORRECT_CREATE_BOOK_MESSAGE_ATTRIBUTE = "Incorrect entered data, please try again";
    private static final String ERROR_CREATE_BOOK_MESSAGE_ATTRIBUTE = "Create book failed";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateBookCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String title = request.getParameter(Constant.TITLE_PARAMETER_NAME);
        final String datePublished = request.getParameter(Constant.DATE_PUBLISHED_PARAMETER_NAME);
        final Integer amountOfLeft = Integer.valueOf(request.getParameter(Constant.AMOUNT_OF_LEFT_PARAMETER_NAME));
        final String authorFirstName = request.getParameter(Constant.FIRST_NAME_PARAMETER_NAME);
        final String authorLastName = request.getParameter(Constant.LAST_NAME_PARAMETER_NAME);
        try {
            if (BasicBookService.getInstance().createBookWithAuthor(title, datePublished, amountOfLeft, authorFirstName, authorLastName)) {
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.catalog"));
            } else {
                request.addAttributeToJsp(ERROR_CREAT_BOOK_MESSAGE_NAME, ERROR_INCORRECT_CREATE_BOOK_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error. could not create book", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_CREATE_BOOK_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.create.book"));
        }
    }

    public static CreateBookCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final CreateBookCommand INSTANCE = new CreateBookCommand();
    }
}
