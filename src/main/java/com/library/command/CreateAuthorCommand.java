package com.library.command;

import com.library.controller.RequestFactory;
import com.library.exception.ServiceException;
import com.library.model.Author;
import com.library.service.BasicAuthorService;
import com.library.util.ConfigurationManager;
import com.library.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CreateAuthorCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(CreateAuthorCommand.class);

    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE_NAME = "errorCreateAuthorMessage";
    private static final String ERROR_INCORRECT_PASS_MESSAGE_ATTRIBUTE = "Author is present or Incorrect dates, please try again";
    private static final String ERROR_PASS_MESSAGE_ATTRIBUTE = "Create author failed";
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CreateAuthorCommand() {
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String firstName = request.getParameter(Constant.FIRST_NAME_PARAMETER_NAME);
        final String lastName = request.getParameter(Constant.LAST_NAME_PARAMETER_NAME);
        final Optional<Author> author;
        try {
            author = BasicAuthorService.getInstance().create(firstName, lastName);
            if (author.isPresent()) {
                return requestFactory.createRedirectResponse(ConfigurationManager.getProperty("url.author"));
            } else {
                request.addAttributeToJsp(ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_INCORRECT_PASS_MESSAGE_ATTRIBUTE);
                return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.create.author"));
            }
        } catch (ServiceException e) {
            LOG.error("Service error, could not create author", e);
            request.addAttributeToJsp(Constant.ERROR_PASS_MESSAGE_ATTRIBUTE_NAME, ERROR_PASS_MESSAGE_ATTRIBUTE);
            return requestFactory.createForwardResponse(ConfigurationManager.getProperty("path.page.error"));
        }
    }

    public static CreateAuthorCommand getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final CreateAuthorCommand INSTANCE = new CreateAuthorCommand();
    }
}
